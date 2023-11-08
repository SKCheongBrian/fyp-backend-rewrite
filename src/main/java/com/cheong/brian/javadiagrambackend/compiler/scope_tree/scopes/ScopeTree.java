package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.github.javaparser.utils.CodeGenerationUtils;

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
    public ClassScope asClassScope() {
        throw new IllegalStateException(CodeGenerationUtils.f("%s is not ClassScope, it is %s",
                this, this.getName(), this.isClassScope()));    }

    @Override
    public MethodScope asMethodScope() {
        throw new IllegalStateException(CodeGenerationUtils.f("%s is not MethodScope, it is %s",
                this, this.getName(), this.isClassScope()));
    }

    @Override
    public boolean isMethodScope() {
        return false;
    }
}
