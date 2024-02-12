package com.cheong.brian.javadiagrambackend.payload.variable;

import java.util.HashMap;
import java.util.Map;

public class ObjectVariable {
    private long id;
    private Map<String, Variable> fields;

    public ObjectVariable(long id) {
        this.id = id;
        this.fields = new HashMap<>();
    }

    public void addField(String fieldName, Variable field) {
        this.fields.put(fieldName, field);
    }

    public long getID() {
        return id;
    }
}
