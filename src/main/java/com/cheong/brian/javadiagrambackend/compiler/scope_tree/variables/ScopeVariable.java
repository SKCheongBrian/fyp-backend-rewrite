package com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables;

import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

@Getter
public abstract class ScopeVariable {
    private final SimpleName name;
    private final boolean isStatic;

    public ScopeVariable(SimpleName name, boolean isStatic) {
        this.name = name;
        this.isStatic = isStatic;
    }

    public abstract boolean isField();

    public abstract boolean isVariable();

}
