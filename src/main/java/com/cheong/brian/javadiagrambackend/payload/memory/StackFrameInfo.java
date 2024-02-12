package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.HashMap;
import java.util.Map;

import com.cheong.brian.javadiagrambackend.payload.variable.Variable;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.StackFrame;
import com.sun.jdi.Value;

public class StackFrameInfo {
    private String methodName;
    private int frameIndex;
    private Map<String, Variable> localVariables;

    private StackFrameInfo(String methodName, int frameIndex) {
        this.methodName = methodName;
        this.frameIndex = frameIndex;
        this.localVariables = new HashMap<>();
    }

    public static StackFrameInfo createStackFrameInfoFromFrame(StackFrame frame, int index) {
        String methodName = frame.location().method().name();

        StackFrameInfo frameInfo = new StackFrameInfo(methodName, index);

        Map<LocalVariable, Value> visibleVariables;
        try {
            visibleVariables = frame.getValues(frame.visibleVariables());
            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                LocalVariable localVariable = entry.getKey();
                Value value = entry.getValue();
                Variable variable = Variable.createVariableFromValue(localVariable.name(), value);
                frameInfo.localVariables.put(localVariable.name(), variable);
            }
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }

        return frameInfo;
    }
}
