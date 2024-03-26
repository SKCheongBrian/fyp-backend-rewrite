package com.cheong.brian.javadiagrambackend.payload.wrappers;

import com.cheong.brian.javadiagrambackend.payload.memory.HeapInfo;
import com.sun.jdi.ObjectReference;

import java.util.Map;

public class ObjectReferenceWrapper implements VisitableWrapper {
    private final ObjectReference objectReference;

    public ObjectReferenceWrapper(ObjectReference objectReference) {
        this.objectReference = objectReference;
    }

    @Override
    public void accept(HeapInfo heapInfo, Map<Long, ObjectReference> idToObj) {
        heapInfo.traverseObjectGraph(objectReference, idToObj);
    }
}
