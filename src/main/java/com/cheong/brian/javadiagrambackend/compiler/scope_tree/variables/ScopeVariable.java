package com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables;

import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

/**
 * An abstract class that encapsulates variables that are in a scope.
 */
@Getter
public abstract class ScopeVariable {
    private final SimpleName name;
    private final boolean isStatic;

    /**
     * The constructor for the ScopeVariable.
     * @param name A SimpleName that corresponds to the name of the ScopeVariable.
     * @param isStatic A boolean that represents if the ScopeVariable is static.
     */
    public ScopeVariable(SimpleName name, boolean isStatic) {
        this.name = name;
        this.isStatic = isStatic;
    }

    /**
     * Checks if the ScopeVariable is a Field.
     * @return True if ScopeVariable is a Field, false otherwise.
     */
    public abstract boolean isField();

    /**
     * Checks if the ScopeVariable is a Variable.
     * @return True if ScopeVariable is a Variable, false otherwise.
     */
    public abstract boolean isVariable();

}
