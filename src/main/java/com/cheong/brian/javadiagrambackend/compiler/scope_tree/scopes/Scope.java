package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

import java.util.HashMap;

/**
 * An abstract class that encapsulates a scope in Java.
 */
@Getter
public abstract class Scope {
    private final HashMap<String, Scope> children;
    private final boolean isStatic;
    private final String name;
    private final Scope parent;

    /**
     * The constructor for the Scope
     * @param name A String that corresponds to the name of the scope.
     * @param parent The parents scope of this scope.
     * @param isStatic A boolean that represents if the scope is static or not.
     */
    public Scope(String name, Scope parent, boolean isStatic) {
        this.name = name;
        this.children = new HashMap<>();
        this.parent = parent;
        this.isStatic = isStatic;
    }

    /**
     * Add a child scope to this scope
     * @param name A String that corresponds to the name of the child scope.
     * @param child The child scope to be added.
     */
    public void addChild(String name, Scope child) {
        this.children.put(name, child);
    }

    /**
     * Checks if a child exists in this scope.
     * @param name String that represents the name of the child.
     * @return True if the child exists, false otherwise.
     */
    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    /**
     * Gets the child name according to the name.
     * @param name A String that corresponds to the name of the child scope.
     * @return The child scope.
     */
    public Scope getChild(String name) {
        return children.get(name);
    }

    /**
     * Gets the name of the scope as a string.
     * @return The string representation of the name of the scope.
     */
    public String getNameAsString() {
        return this.name;
    }

    /**
     * Checks if the scope is a class scope.
     * @return True if a class scope, false otherwise.
     */
    public abstract boolean isClassScope();

    /**
     * Returns the same Scope but with compile time type of ClassScope.
     * @return The ClassScope.
     */
    public abstract ClassScope asClassScope();

    /**
     * Returns the same Scope but with compile time type of MethodScope.
     * @return The MethodScope.
     */
    public abstract MethodScope asMethodScope();

    /**
     * Checks if the scope is a method scope.
     * @return True if a method scope, false otherwise.
     */
    public abstract boolean isMethodScope();
}
