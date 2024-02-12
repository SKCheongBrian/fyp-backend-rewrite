package com.cheong.brian.javadiagrambackend.payload;

import java.util.ArrayList;
import java.util.List;

public class ProgramData {
    private List<StepInfo> stepInfos;

    public ProgramData() {
        this.stepInfos = new ArrayList<>();
    }

    public void addStep(StepInfo stepInfo) {
        this.stepInfos.add(stepInfo);
    }
}
