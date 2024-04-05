package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.*;

import com.cheong.brian.javadiagrambackend.payload.variable.ObjectVariable;
import com.cheong.brian.javadiagrambackend.payload.variable.PrimitiveVariable;
import com.cheong.brian.javadiagrambackend.payload.variable.Variable;
import com.cheong.brian.javadiagrambackend.payload.wrappers.WrapperFactory;
import com.sun.jdi.*;

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

    public void populate(StackFrame frame, HashMap<Long, ObjectReference> idToObj) {
        for (Long objId : idToObj.keySet()) {
            ObjectReference objectReference = idToObj.get(objId);
            if (!objectReference.isCollected()) {
                traverseObjectGraph(objectReference, idToObj);
            }
        }
        try {
            populateFromVariables(frame.getValues(frame.visibleVariables()), idToObj);
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
        ObjectReference thisRef = frame.thisObject();
        if (thisRef != null) {
            traverseObjectGraph(thisRef, idToObj);
        }
    }

    private void populateFromVariables(Map<LocalVariable, Value> variables, Map<Long, ObjectReference> idToObj) {
        for (Map.Entry<LocalVariable, Value> entry : variables.entrySet()) {
            Value value = entry.getValue();
            if (value instanceof ObjectReference) {
                WrapperFactory.create(value).accept(this, idToObj);
            }
        }
    }

    public void traverseObjectGraph(ArrayReference arrayReference, Map<Long, ObjectReference> idToObj) {
        if ("[Ljava/lang/String;".equals(arrayReference.type().signature())) {
            List<Value> values = arrayReference.getValues();
            StringBuilder value = new StringBuilder();

            for (Value v : values) {
                value.append(v.toString());
            }
            long objectId = arrayReference.uniqueID();
            ObjectVariable objectVariable = createObjectVariable(arrayReference);
            addObject(objectId, objectVariable);
            idToObj.put(objectId, arrayReference);
            objectVariable.addField("value",
                    new PrimitiveVariable("value", PrimitiveVariable.Type.STRING, value.toString()));
        } else {
            long objectId = arrayReference.uniqueID();
            if (!visitedObjects.contains(objectId)) {
                visitedObjects.add(objectId);
                if (arrayReference.referenceType().name().equals("java.lang.String")) {
                    Field stringValueField = arrayReference.referenceType().fieldByName("value");
                    if (stringValueField != null) {
                        Value stringValue = arrayReference.getValue(stringValueField);
                        if (stringValue != null && stringValue instanceof ArrayReference) {
                            ArrayReference stringCharArray = (ArrayReference) stringValue;
                            StringBuilder sb = new StringBuilder();
                            for (Value charValue : stringCharArray.getValues()) {
                                sb.append(((PrimitiveValue) charValue).charValue());
                            }
                            String string = sb.toString();
                            ObjectVariable objectVariable = createObjectVariable(arrayReference);
                            addObject(objectId, objectVariable);
                            idToObj.put(objectId, arrayReference);
                            objectVariable.addField("value", new PrimitiveVariable("value",
                                    PrimitiveVariable.Type.STRING, string));
                        }
                    }
                } else {
                    ObjectVariable objectVariable = createObjectVariable(arrayReference);
                    addObject(objectId, objectVariable);
                    idToObj.put(objectId, arrayReference);
                    populateObjectVariableFields(objectVariable, arrayReference);

                    for (Field field : arrayReference.referenceType().allFields()) {
                        if (!field.isStatic()) {
                            Value fieldValue = arrayReference.getValue(field);
                            if (fieldValue instanceof ObjectReference) {
                                WrapperFactory.create(fieldValue).accept(this, idToObj);
                            }
                        }
                    }
                }
            }
        }
    }

    public void traverseObjectGraph(ObjectReference objectReference, Map<Long, ObjectReference> idToObj) {
        long objectId = objectReference.uniqueID();
        if (!visitedObjects.contains(objectId)) {
            visitedObjects.add(objectId);
            if (objectReference.referenceType().name().equals("java.lang.String")) {
                Field stringValueField = objectReference.referenceType().fieldByName("value");
                if (stringValueField != null) {
                    Value stringValue = objectReference.getValue(stringValueField);
                    if (stringValue != null && stringValue instanceof ArrayReference) {
                        ArrayReference stringCharArray = (ArrayReference) stringValue;
                        StringBuilder sb = new StringBuilder();
                        for (Value charValue : stringCharArray.getValues()) {
                            sb.append(((PrimitiveValue) charValue).charValue());
                        }
                        String string = sb.toString();
                        ObjectVariable objectVariable = createObjectVariable(objectReference);
                        addObject(objectId, objectVariable);
                        idToObj.put(objectId, objectReference);
                        objectVariable.addField("value", new PrimitiveVariable("value",
                                PrimitiveVariable.Type.STRING, string));
                    }
                }
            } else {
                ObjectVariable objectVariable = createObjectVariable(objectReference);
                addObject(objectId, objectVariable);
                idToObj.put(objectId, objectReference);
                populateObjectVariableFields(objectVariable, objectReference);

                for (Field field : objectReference.referenceType().allFields()) {
                    if (!field.isStatic()) {
                        Value fieldValue = objectReference.getValue(field);
                        if (fieldValue instanceof ObjectReference) {
                            WrapperFactory.create(fieldValue).accept(this, idToObj);
                        }
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
