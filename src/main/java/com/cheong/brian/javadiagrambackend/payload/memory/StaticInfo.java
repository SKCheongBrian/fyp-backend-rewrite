package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheong.brian.javadiagrambackend.payload.variable.Variable;
import com.sun.jdi.Field;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;

public class StaticInfo {
    private Map<String, StaticClass> staticVariables;

    public StaticInfo() {
        this.staticVariables = new HashMap<>();
    }

    public void populate(List<ReferenceType> classes, HeapInfo heapInfo) {
        for (ReferenceType referenceType : classes) {
            List<Field> fields = referenceType.fields();
            String className = referenceType.name();
            StaticClass staticClass = new StaticClass(className);
            for (Field field : fields) {
                if (!field.isStatic()) {
                    continue;
                }
                Value value = referenceType.getValue(field);
                Variable staticField = Variable.createVariableFromValue(field.name(), value);
                if (!this.staticVariables.containsKey(className)) {
                    this.staticVariables.put(className, staticClass);
                }
                staticClass.addVariable(staticField);
                heapInfo.addObjectFromValue(value);
            }
        }

    }
}
