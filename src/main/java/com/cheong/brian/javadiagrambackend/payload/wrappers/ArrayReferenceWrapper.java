package com.cheong.brian.javadiagrambackend.payload.wrappers;

import com.cheong.brian.javadiagrambackend.payload.memory.HeapInfo;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.ObjectReference;

import java.util.Map;

public class ArrayReferenceWrapper implements VisitableWrapper {
    private final ArrayReference arrayReference;

    public ArrayReferenceWrapper(ArrayReference arrayReference) {
        this.arrayReference = arrayReference;
    }

    @Override
    public void accept(HeapInfo heapInfo, Map<Long, ObjectReference> idToObj) {
        heapInfo.traverseObjectGraph(arrayReference, idToObj);
    }
}
