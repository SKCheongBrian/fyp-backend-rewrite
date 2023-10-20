package com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables;

import com.github.javaparser.ast.expr.SimpleName;

public class Variable extends ScopeVariable {
    public Variable(SimpleName name, boolean isStatic) {
        super(name, isStatic);
    }

    @Override
    public boolean isField() {
        return false;
    }

    @Override
    public boolean isVariable() {
        return true;
    }
}
