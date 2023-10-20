package com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes;

import com.github.javaparser.ast.expr.SimpleName;
import lombok.Getter;

import java.util.HashMap;

@Getter
public abstract class Scope {
    private final HashMap<SimpleName, Scope> children;
    private final boolean isStatic;
    private SimpleName name;
    private Scope parent;

    public Scope(SimpleName name, Scope parent, boolean isStatic) {
        this.name = name;
        this.children = new HashMap<>();
        this.parent = parent;
        this.isStatic = isStatic;
    }

    public void setParent(Scope parentScope) {
        this.parent = parentScope;
    }

    public void addChild(SimpleName name, Scope child) {
        this.children.put(name, child);
    }

    public boolean hasChild(SimpleName name) {
        return children.containsKey(name);
    }

    public String getNameAsString() {
        return this.name.asString();
    }

    public abstract boolean isClassScope();

    public abstract boolean isMethodScope();
}
