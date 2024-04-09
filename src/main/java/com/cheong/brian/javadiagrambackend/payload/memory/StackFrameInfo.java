package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cheong.brian.javadiagrambackend.payload.variable.Variable;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.StackFrame;
import com.sun.jdi.Value;

public class StackFrameInfo {
    private String methodName;
    private int frameIndex;
    // private Map<String, Variable> localVariables;
    private List<Variable> localVariables;

    private StackFrameInfo(String methodName, int frameIndex) {
        this.methodName = methodName;
        this.frameIndex = frameIndex;
        this.localVariables = new ArrayList<>();
    }

    public static StackFrameInfo createStackFrameInfoFromFrame(StackFrame frame, int index) {
        String methodName = frame.location().method().name();

        StackFrameInfo frameInfo = new StackFrameInfo(methodName, index);

        List<LocalVariable> visibleVariables;

        try {
            visibleVariables = frame.visibleVariables();
            for (LocalVariable localVariable : visibleVariables) {
                Value value = frame.getValue(localVariable);
                Variable variable = Variable.createVariableFromValue(localVariable.name(), value);
                frameInfo.localVariables.add(variable);
            }
            Collections.reverse(frameInfo.localVariables);
            ObjectReference thisReference = frame.thisObject();
            if (thisReference != null) {
                String thisString = "this";
                Variable thisRefVar = Variable.createVariableFromValue(thisString, thisReference);
                frameInfo.localVariables.add(thisRefVar);
            }
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }

        return frameInfo;
    }
}
