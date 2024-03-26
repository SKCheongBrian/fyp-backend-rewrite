package com.cheong.brian.javadiagrambackend.payload.wrappers;

import com.cheong.brian.javadiagrambackend.payload.memory.HeapInfo;
import com.sun.jdi.ObjectReference;

import java.util.Map;

public interface VisitableWrapper {
    public void accept(HeapInfo heapInfo, Map<Long, ObjectReference> idToObj);
}
