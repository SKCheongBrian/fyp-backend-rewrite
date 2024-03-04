package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.*;

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
    private Set<Long> visitedObjects;

    public HeapInfo() {
        this.heapObjects = new HashMap<>();
        this.visitedObjects = new HashSet<>();
    }

    public void addObject(long id, ObjectVariable obj) {
        heapObjects.put(id, obj);
    }

    public void populate(StackFrame frame) {
        try {
            populateFromVariables(frame.getValues(frame.visibleVariables()));
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
        ObjectReference thisRef = frame.thisObject();
        this.addObjectFromValue(thisRef);
    }

    private void populateFromVariables(Map<LocalVariable, Value> variables) {
        for (Map.Entry<LocalVariable, Value> entry : variables.entrySet()) {
            Value value = entry.getValue();
            if (value instanceof ObjectReference) {
                ObjectReference objectReference = (ObjectReference) value;
                traverseObjectGraph(objectReference);
            }
        }
    }

    private void traverseObjectGraph(ObjectReference objectReference) {
        long objectId = objectReference.uniqueID();
        if (!visitedObjects.contains(objectId)) {
            visitedObjects.add(objectId);
            ObjectVariable objectVariable = createObjectVariable(objectReference);
            addObject(objectId, objectVariable);
            populateObjectVariableFields(objectVariable, objectReference);

            for (Field field : objectReference.referenceType().allFields()) {
                if (!field.isStatic()) {
                    Value fieldValue = objectReference.getValue(field);
                    if (fieldValue instanceof ObjectReference) {
                        traverseObjectGraph((ObjectReference) fieldValue);
                    }
                }
            }
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
