package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Variable;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import lombok.Getter;

import java.util.HashMap;

/**
 * A class that encapsulates the scope of a method in Java.
 */
@Getter
public class MethodScope extends Scope {
    private final HashMap<SimpleName, Variable> variables;
    private final Type returnType;
    private final NodeList<TypeParameter> typeParameters;
    private final NodeList<Modifier> modifiers;

    /**
     * The constructor for the MethodScope.
     * @param name SimpleName that corresponds to the name of the method.
     * @param parent Scope that is the parent of the method.
     * @param isStatic A boolean that represents if the method is static.
     * @param type The return type of the method.
     * @param typeParameters NodeList of types of the parameters of the method.
     * @param modifiers NodeList of the modifiers of the method.
     */
    public MethodScope(SimpleName name, Scope parent, boolean isStatic, Type type,
                       NodeList<TypeParameter> typeParameters, NodeList<Modifier> modifiers) {
        super(name, parent, isStatic);
        this.variables = new HashMap<>();
        this.returnType = type;
        this.typeParameters = typeParameters;
        this.modifiers = modifiers;
    }

    /**
     * Adds a variable to a method scope.
     * @param variable The variable to be added.
     */
    public void addVariable(Variable variable) {
        this.variables.put(variable.getName(), variable);
    }

    @Override
    public boolean isClassScope() {
        return false;
    }

    @Override
    public boolean isMethodScope() {
        return true;
    }
}
