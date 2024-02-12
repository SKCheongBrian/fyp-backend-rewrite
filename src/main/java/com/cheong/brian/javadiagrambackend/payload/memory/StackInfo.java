package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.ArrayList;
import java.util.List;

public class StackInfo {
    private List<StackFrameInfo> stackFrames;

    public StackInfo() {
        this.stackFrames = new ArrayList<>();
    }

    public void addFrame(StackFrameInfo stackFrameInfo) {
        this.stackFrames.add(stackFrameInfo);
    }
}
