package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Field;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.utils.CodeGenerationUtils;
import lombok.Getter;

import java.util.HashMap;

/**
 * A Scope that represents the scope of a class in Java.
 */
@Getter
public class ClassScope extends Scope {
    private final HashMap<SimpleName, Field> fields;
    private ClassScope superClassScope;

    /**
     * The constructor for the ClassScope.
     *
     * @param name     a SimpleName that corresponds to the name of the class.
     * @param parent   The parent scope of the class.
     * @param isStatic Boolean that represents if the class is static.
     */
    public ClassScope(SimpleName name, Scope parent, boolean isStatic) {
        super(name, parent, isStatic);
        this.fields = new HashMap<>();
    }

    /**
     * Adds a field to the class scope.
     *
     * @param field The field to be added.
     */
    public void addField(Field field) {
        this.fields.put(field.getName(), field);
    }

    @Override
    public ClassScope asClassScope() {
        return this;
    }

    @Override
    public MethodScope asMethodScope() {
        throw new IllegalStateException(CodeGenerationUtils.f("%s is not ClassOrInterfaceDeclaration, it is %s",
                this, this.getName(), this.isMethodScope()));
    }

    /**
     * Adds the super scope to this class scope.
     *
     * @param superClassScope the super class scope to be added.
     */
    public void addSuperScope(ClassScope superClassScope) {
        this.superClassScope = superClassScope;
    }

    @Override
    public boolean isClassScope() {
        return true;
    }

    @Override
    public boolean isMethodScope() {
        return false;
    }
}
