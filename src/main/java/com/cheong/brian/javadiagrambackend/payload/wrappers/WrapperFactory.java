package com.cheong.brian.javadiagrambackend.payload.wrappers;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;

public class WrapperFactory {
    public static VisitableWrapper create(Value v) {
        if (v instanceof ArrayReference) {
            return new ArrayReferenceWrapper((ArrayReference) v);
        } else if (v instanceof ObjectReference) {
            return new ObjectReferenceWrapper((ObjectReference) v);
        } else {
            return null;
        }
    }
}
