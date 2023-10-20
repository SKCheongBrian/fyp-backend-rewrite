package com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables;

import com.github.javaparser.ast.expr.SimpleName;

/**
 * A class that represents a variable in a method scope in Java.
 */
public class Variable extends ScopeVariable {
    /**
     * The constructor for the Variable.
     * @param name A SimpleName that corresponds to the
     * @param isStatic
     */
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
