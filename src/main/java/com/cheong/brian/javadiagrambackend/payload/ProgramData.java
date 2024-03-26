package com.cheong.brian.javadiagrambackend.payload;

import java.util.ArrayList;
import java.util.List;

public abstract class ProgramData {
    public static ProgramData success() {
        return new Success();
    }

    public static ProgramData failure(String errorMessage) {
        return new Failure(errorMessage);
    }

    public abstract void addStep(StepInfo stepInfo);

    private static class Failure extends ProgramData {
        String errorMessage;
        private final boolean isSuccess = false;

        private Failure(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public void addStep(StepInfo stepInfo) {
            throw new UnsupportedOperationException("Cannot add step to a failed program");
        }

    }

    private static class Success extends ProgramData {
        private List<StepInfo> stepInfos;
        private final boolean isSuccess = true;

        private Success() {
            this.stepInfos = new ArrayList<>();
        }

        @Override
        public void addStep(StepInfo stepInfo) {
            this.stepInfos.add(stepInfo);
        }
    }
}
