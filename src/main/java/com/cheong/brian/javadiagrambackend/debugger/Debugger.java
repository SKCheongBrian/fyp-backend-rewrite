package com.cheong.brian.javadiagrambackend.debugger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.StepRequest;

public class Debugger {

    private final VirtualMachine vm;
    private final EventRequestManager eventReqMan;
    private Location lastLocation;
    private int stepCount;
    private final int stepLimit = 100;

    public Debugger(String className) throws Exception {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("options").setValue("-cp ./sandbox/");
        arguments.get("main").setValue(className);
        arguments.get("suspend").setValue("true");
        vm = launchingConnector.launch(arguments);

        String workingDirectory = System.getProperty("user.dir");
        System.out.println(workingDirectory);

        eventReqMan = vm.eventRequestManager();
        this.prepareAndLaunchClass(className);
    }

    private void prepareAndLaunchClass(String className) {
        ClassPrepareRequest classPrepareRequest = eventReqMan.createClassPrepareRequest();
        classPrepareRequest.addClassFilter(className);
        classPrepareRequest.enable();
    }

    public void stepThroughClass() {
        boolean stillRunning = true;
        this.stepCount = 0;
        try {
            while (stillRunning && stepCount < stepLimit) {
                EventSet events = vm.eventQueue().remove();
                stillRunning = processEvents(events);
            }
        } catch (VMDisconnectedException e) {
            System.err.println("[Error]: VM has disconnected (VMDisconnectedException)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stepCount == stepLimit) {
            System.err.println("[Error]: Maximum step count has been reached!");
        }
    }

    private boolean processEvents(EventSet events) throws AbsentInformationException {
        EventIterator iter = events.eventIterator();
        boolean isRunning = true;
        while (iter.hasNext()) {
            Event event = iter.nextEvent();
            isRunning = handleEvent(event);
            vm.resume();
        }
        return isRunning;
    }

    private boolean handleEvent(Event event) throws AbsentInformationException {
        if (event instanceof ClassPrepareEvent) {
            return handleClassPrepareEvent((ClassPrepareEvent) event);
        } else if (event instanceof BreakpointEvent) {
            return handleBreakpointEvent((BreakpointEvent) event);
        } else if (event instanceof StepEvent) {
            return handleStepEvent((StepEvent) event);
        } else if (event instanceof ExceptionEvent) {
            return handleExceptionEvent((ExceptionEvent) event);
        }
        return true;
    }

    private boolean handleClassPrepareEvent(ClassPrepareEvent event) throws AbsentInformationException {
        System.out.println("THERE IS A CPE!");
        ClassType classType = (ClassType) event.referenceType();
        System.out.println(classType.name());

        classType.methodsByName("main").forEach(method -> {
            try {
                List<Location> locations = method.allLineLocations();
                Location location = locations.get(0);
                this.lastLocation = locations.get(locations.size() - 1);
                System.out.println("Line number: " + location.lineNumber());
                BreakpointRequest bpReq = eventReqMan.createBreakpointRequest(location);
                bpReq.enable();
            } catch (AbsentInformationException e) {
                System.err.println(e);
            }
        });
        return true;
    }

    private boolean handleBreakpointEvent(BreakpointEvent event) {
        event.request().disable();
        ThreadReference thread = event.thread();
        StackFrame frame = null;
        try {
            frame = thread.frame(0);
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
        Location currentLocation = frame.location();
        Method method = currentLocation.method();

        if (this.isUserMethod(method)) {
            this.stepCount++;
            printDebugInfo(currentLocation, frame);
        }
        if (currentLocation.equals(this.lastLocation)) {
            System.out.println("DONE DEBUGGING!!!");
            return false;
        }

        StepRequest stepReq = createStepRequest(thread, method);

        // Need this for exceptions
        ExceptionRequest exceptionRequest = eventReqMan.createExceptionRequest(null, true, true);

        exceptionRequest.enable();
        stepReq.enable();
        return true;
    }

    private StepRequest createStepRequest(ThreadReference thread, Method method) {
        int stepOption = isUserMethod(method) ? StepRequest.STEP_INTO : StepRequest.STEP_OVER;
        return eventReqMan.createStepRequest(thread, StepRequest.STEP_LINE, stepOption);
    }

    private boolean handleStepEvent(StepEvent event) {
        event.request().disable();
        StackFrame frame = null;
        try {
            frame = event.thread().frame(0);
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
        Location currentLocation = frame.location();
        Method method = currentLocation.method();
        if (this.isUserMethod(method)) {
            stepCount++;
            printDebugInfo(currentLocation, frame);
        }

        if (currentLocation.equals(lastLocation)) {
            System.out.println("DONE DEBUGGING!!!");
            return false;
        }

        StepRequest stepReq = createStepRequest(event.thread(), method);
        stepReq.enable();
        return true;
    }

    private boolean handleExceptionEvent(ExceptionEvent event) {
        System.out.println("EVERYONE PANIC! THERE'S AN EXCEPTION!!!");
        if (event.catchLocation() == null) {
            ObjectReference exception = event.exception();
            Field detailMessageField = exception.referenceType().fieldByName("detailMessage");
            Value detailMessageValue = exception.getValue(detailMessageField);

            String errorMessage = detailMessageValue.toString();
            String exceptionTypeString = exception.referenceType().name();
            String threadName = event.thread().name();
            String stackTraceStart = "Exception in thread \"" + threadName + "\" "
                    + exceptionTypeString + ": " + errorMessage + "\n";

            StringBuilder stackTrace = new StringBuilder(stackTraceStart);
            List<StackFrame> frames = null;
            try {
                frames = event.thread().frames();
            } catch (IncompatibleThreadStateException e) {
                e.printStackTrace();
            }

            for (StackFrame f : frames) {
                Location loc = f.location();
                try {
                    stackTrace.append("\tat ").append(loc.declaringType().name().replace("org.test", ""))
                            .append(".").append(loc.method().name())
                            .append("(").append(loc.sourcePath().replace("org/test/", "")).append(":")
                            .append(loc.lineNumber()).append(")\n");
                } catch (AbsentInformationException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(stackTrace.toString());

            return false;
        }
        return true;
    }

    private void printDebugInfo(Location currentLocation, StackFrame frame) {
        System.out.println("===================================================\n");
        try {
            System.out.println("Line: " + currentLocation.lineNumber() + " - " + currentLocation.sourceName());
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
        printStaticInfo();
        Map<LocalVariable, Value> visibleVariables = null;
        try {
            visibleVariables = frame.getValues(frame.visibleVariables());
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
        for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
            LocalVariable localVariable = entry.getKey();
            Value val = entry.getValue();
            System.out.println(localVariable + ": " + val);
            if (val instanceof ObjectReference) {
                printObjectReferenceInfo((ObjectReference) val);
            }
            System.out.println("----------------------------------------------");
        }
        System.out.println("===================================================\n");
    }

    private void printObjectReferenceInfo(ObjectReference objRef) {
        ReferenceType refType = objRef.referenceType();
        System.out.println("  - Type: " + refType.name());
        for (Field field : refType.fields()) {
            try {
                Value fieldValue = objRef.getValue(field);
                System.out.println("    * " + field.name() + ": " + fieldValue);
            } catch (Exception e) {
                System.err.println("    * Error getting field: '" + field.name() + "': " + e);
            }
        }
    }

    private void printStaticInfo() {
        List<ReferenceType> referenceTypes = vm.allClasses();

        for (ReferenceType referenceType : referenceTypes) {
            // only want to print for user defined classes
            if (!isClassInApplicationPackage(referenceType)) continue;

            List<Field> fields = referenceType.fields();

            for (Field field : fields) {
                if (field.isStatic()) {
                    Value value = referenceType.getValue(field);
                    System.out.println("Static variable in " + referenceType.name() +
                            ": " + field.name() + " = " + value);
                }
            }
        }
    }

    private boolean isUserMethod(Method method) {
        System.out.println("CHECKING FOR METHOD: " + method.name());
        return isClassInApplicationPackage(method.declaringType());
    }

    private boolean isClassInApplicationPackage(ReferenceType refType) {
        String packageName = refType.name();
        return packageName.startsWith("org.test"); // Adjust the package name accordingly
    }
}