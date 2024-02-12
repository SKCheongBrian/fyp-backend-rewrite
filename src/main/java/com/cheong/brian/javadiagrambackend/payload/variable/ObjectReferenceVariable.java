package com.cheong.brian.javadiagrambackend.payload.variable;

public class ObjectReferenceVariable extends Variable {
    private long id;

    public ObjectReferenceVariable(String name, long id) {
        super(name);
        this.id = id;
    }
}
