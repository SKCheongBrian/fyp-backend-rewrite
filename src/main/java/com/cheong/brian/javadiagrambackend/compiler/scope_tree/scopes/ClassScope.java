package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Field;
import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class ClassScope extends Scope {
    private final HashMap<SimpleName, Field> fields;
    private ClassScope superClassScope;

    public ClassScope(SimpleName name, Scope parent, boolean isStatic) {
        super(name, parent, isStatic);
        this.fields = new HashMap<>();
    }

    public void addField(Field field) {
        this.fields.put(field.getName(), field);
    }

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
