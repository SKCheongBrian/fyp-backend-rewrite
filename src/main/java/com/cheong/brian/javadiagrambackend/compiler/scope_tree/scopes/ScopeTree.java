package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

/**
 * A class that encapsulates the scope tree of a Java program.
 */
public class ScopeTree extends Scope {
    /**
     * Constructor of the scope tree.
     */
    public ScopeTree() {
        super(null, null, false);
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
