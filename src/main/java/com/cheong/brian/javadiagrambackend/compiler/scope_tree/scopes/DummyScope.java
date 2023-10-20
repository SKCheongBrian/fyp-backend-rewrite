package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

@Getter
public class DummyScope extends Scope {
    public DummyScope() {
        super(new SimpleName("*DUMMY*"), null, false);
    }

    @Override
    public boolean isClassScope() {
        return false;
    }

    @Override
    public boolean isMethodScope() {
        return false;
    }
}
