package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.github.javaparser.utils.CodeGenerationUtils;

/**
 * A class that encapsulates the scope tree of a Java program.
 */
public class ScopeTree extends ClassScope {
    /**
     * Constructor of the scope tree.
     */
    public ScopeTree() {
        super(null, null, true);
    }
}
