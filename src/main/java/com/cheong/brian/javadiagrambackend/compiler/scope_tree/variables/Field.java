package com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables;

import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

/**
 * Class that encapsulates a field for a class scope in Java.
 */
@Getter
public class Field extends ScopeVariable {
    /**
     * The constructor for the field.
     * @param name A SimpleName that corresponds to the name of the Field.
     * @param isStatic A boolean that represents if the field is static.
     */
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
