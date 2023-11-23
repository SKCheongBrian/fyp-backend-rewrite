package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Field;
import com.github.javaparser.utils.CodeGenerationUtils;
import lombok.Getter;

import java.util.HashMap;

/**
 * A Scope that represents the scope of a class in Java.
 */
@Getter
public class ClassScope extends Scope {
    private final HashMap<String, Field> fields;
    private ClassScope superClassScope;

    /**
     * The constructor for the ClassScope.
     *
     * @param name     a String that corresponds to the name of the class.
     * @param parent   The parent scope of the class.
     * @param isStatic Boolean that represents if the class is static.
     */
    public ClassScope(String name, Scope parent, boolean isStatic) {
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

    /**
     * Returns true if the given string is an instance of the unambiguous name, namely it matches from the last element
     * all the way to the first element of the string that is split by a period.
     *
     * @param other The string to be matched
     * @return Boolean that is True if it is an instance, False otherwise
     */
    public boolean matchesUnambiguousName(String other) {
        String[] unambiguousSplit = this.getName().split("\\.");
        String[] otherSplit = other.split("\\.");
        int unambiguousPointer = unambiguousSplit.length - 1;
        for (int otherPointer = otherSplit.length - 1; otherPointer >= 0; otherPointer--) {
            if (unambiguousPointer < 0 || !unambiguousSplit[unambiguousPointer].equals(otherSplit[otherPointer])) {
                return false;
            }
            unambiguousPointer--;
        }
        return true;
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
