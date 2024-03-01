package com.cheong.brian.javadiagrambackend.payload.variable;

import java.util.HashMap;
import java.util.Map;

public class ObjectVariable {
    private long id;
    private String className;
    private Map<String, Variable> fields;
    private Map<String, Variable> syntheticFields;

    public ObjectVariable(long id, String className) {
        this.id = id;
        this.className = className;
        this.fields = new HashMap<>();
        this.syntheticFields = new HashMap<>();
    }

    public void addField(String fieldName, Variable field) {
        this.fields.put(fieldName, field);
    }

    public void addSyntheticField(String fieldName, Variable field) {
        this.syntheticFields.put(fieldName, field);
    }

    public long getID() {
        return id;
    }
}
