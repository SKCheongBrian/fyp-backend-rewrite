package com.cheong.brian.javadiagrambackend.payload.memory;

import java.util.ArrayList;
import java.util.List;

import com.cheong.brian.javadiagrambackend.payload.variable.Variable;

public class StaticClass {
    private List<Variable> staticVariables;
    private String className;

    public StaticClass(String className) {
        this.className = className;
        this.staticVariables = new ArrayList<>();
    }

    public void addVariable(Variable v) {
        this.staticVariables.add(v);
    }
}
