package com.cheong.brian.javadiagrambackend.payload.variable;

public class PrimitiveVariable extends Variable {
    public enum Type {
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BOOLEAN,
        CHAR,
        UNKNOWN
    }

    private Type type;
    private String value;

    public PrimitiveVariable(String name,Type type, String value) {
        super(name);
        this.type = type;
        this.value = value;
    } 
}
