package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheong.brian.javadiagrambackend.payload.variable.ObjectVariable;
import com.cheong.brian.javadiagrambackend.payload.variable.Variable;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Field;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.StackFrame;
import com.sun.jdi.Value;

public class HeapInfo {
    private Map<Long, ObjectVariable> heapObjects;

    public HeapInfo() {
        this.heapObjects = new HashMap<>();
    }

    public void addObject(long id, ObjectVariable obj) {
        heapObjects.put(id, obj);
    }

    public void populate(StackFrame frame) {
        Map<LocalVariable, Value> visibleVariables = null;
        try {
            visibleVariables = frame.getValues(frame.visibleVariables());
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }

        if (visibleVariables == null) {
            return;
        }
        for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
            Value value = entry.getValue();
            this.addObjectFromValue(value);
        }
    }

    public void addObjectFromValue(Value value) {
        if (value instanceof ObjectReference) {
            ObjectReference objectReference = (ObjectReference) value;
            ObjectVariable objectVariable = HeapInfo.createObjectVariable(objectReference);
            HeapInfo.populateObjectVariableFields(objectVariable, objectReference);
            this.addObject(objectVariable.getID(), objectVariable);
        }
    }

    private static ObjectVariable createObjectVariable(ObjectReference objectReference) {
        return new ObjectVariable(objectReference.uniqueID(), objectReference.referenceType().name());
    }

    private static void populateObjectVariableFields(ObjectVariable objectVariable, ObjectReference objectReference) {
        List<Field> syntheticFields = new ArrayList<>();
        for (Field field : objectReference.referenceType().allFields()) {
            if (field.isStatic()) {
                continue;
            }
            if (field.isSynthetic()) {
                syntheticFields.add(field);
            } else {
                Value fieldValue = objectReference.getValue(field);
                Variable fieldVariable = Variable.createVariableFromValue(field.name(), fieldValue);
                String name = field.name();
                objectVariable.addField(name, fieldVariable);
            }
        }
        // add synthetic fields
        for (Field field : syntheticFields) {
            Value fieldValue = objectReference.getValue(field);
            Variable fieldVariable = Variable.createVariableFromValue(field.name(), fieldValue);
            String name = field.name();
            objectVariable.addSyntheticField(name, fieldVariable);
        }
    }

}
