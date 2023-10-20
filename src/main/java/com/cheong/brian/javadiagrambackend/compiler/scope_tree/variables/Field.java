package com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables;

import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

@Getter
public class Field extends ScopeVariable {
    public Field(SimpleName name, boolean isStatic) {
        super(name, isStatic);
    }

    @Override
    public boolean isField() {
        return true;
    }

    @Override
    public boolean isVariable() {
        return false;
    }
}
