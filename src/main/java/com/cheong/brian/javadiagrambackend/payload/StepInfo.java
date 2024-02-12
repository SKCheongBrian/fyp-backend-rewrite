package com.cheong.brian.javadiagrambackend.payload;

import com.cheong.brian.javadiagrambackend.payload.memory.HeapInfo;
import com.cheong.brian.javadiagrambackend.payload.memory.StackInfo;
import com.cheong.brian.javadiagrambackend.payload.memory.StaticInfo;

public class StepInfo {
    private int stepNumber;
    private int lineNumber;
    private StackInfo stackInfo;
    private HeapInfo heapInfo;
    private StaticInfo staticInfo;

    public StepInfo(int stepNumber, int lineNumber, StackInfo stackInfo, HeapInfo heapInfo, StaticInfo staticInfo) {
        this.stepNumber = stepNumber;
        this.lineNumber = lineNumber;
        this.stackInfo = stackInfo;
        this.heapInfo = heapInfo;
        this.staticInfo = staticInfo;
    }
}
