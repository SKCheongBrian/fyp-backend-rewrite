package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Variable;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class MethodScope extends Scope {
    HashMap<SimpleName, Variable> variables;
    Type returnType;
    NodeList<TypeParameter> typeParameters;
    NodeList<Modifier> modifiers;

    public MethodScope(SimpleName name, Scope parent, boolean isStatic, Type type,
                       NodeList<TypeParameter> typeParameters, NodeList<Modifier> modifiers) {
        super(name, parent, isStatic);
        this.variables = new HashMap<>();
        this.returnType = type;
        this.typeParameters = typeParameters;
        this.modifiers = modifiers;
    }


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
