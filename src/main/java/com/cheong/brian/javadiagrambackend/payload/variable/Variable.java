package com.cheong.brian.javadiagrambackend.payload.variable;

import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LongValue;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ShortValue;
import com.sun.jdi.Value;

public class Variable {
    private String name;

    public Variable(String variableName) {
        this.name = variableName;
    }

    public static Variable createVariableFromValue(String name, Value value) {
        if (value == null) {
            return new PrimitiveVariable(name, PrimitiveVariable.Type.UNKNOWN, "null");
        }

        if (value instanceof ObjectReference) {
            ObjectReference objectReference = (ObjectReference) value;
            return new ObjectReferenceVariable(name, objectReference.uniqueID());
        }

        if (value instanceof PrimitiveValue) {
            PrimitiveValue primitiveValue = (PrimitiveValue) value;
            if (primitiveValue instanceof BooleanValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.BOOLEAN, Boolean.toString(((BooleanValue) primitiveValue).value()));
            } else if (primitiveValue instanceof ByteValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.BYTE, Byte.toString(((ByteValue) primitiveValue).value()));
            } else if (primitiveValue instanceof CharValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.CHAR, Character.toString(((CharValue) primitiveValue).value()));
            } else if (primitiveValue instanceof DoubleValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.DOUBLE, Double.toString(((DoubleValue) primitiveValue).value()));
            } else if (primitiveValue instanceof FloatValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.FLOAT, Float.toString(((FloatValue) primitiveValue).value()));
            } else if (primitiveValue instanceof IntegerValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.INT, Integer.toString(((IntegerValue) primitiveValue).value()));
            } else if (primitiveValue instanceof LongValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.LONG, Long.toString(((LongValue) primitiveValue).value()));
            } else if (primitiveValue instanceof ShortValue) {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.SHORT, Short.toString(((ShortValue) primitiveValue).value()));
            } else {
                return new PrimitiveVariable(name, PrimitiveVariable.Type.UNKNOWN, "Unknown primitive type");
            }
        }
        return new PrimitiveVariable(name, PrimitiveVariable.Type.UNKNOWN, "null");
    }
}
