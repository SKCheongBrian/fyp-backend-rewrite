package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.scope_constructors;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.ScopeTree;
import com.github.javaparser.ast.CompilationUnit;

public class ScopeTreeProducer {
    private final ScopeTreeSkeletonMakerVisitor scopeTreeSkeletonMakerVisitor;

    public ScopeTreeProducer() {
        this.scopeTreeSkeletonMakerVisitor = new ScopeTreeSkeletonMakerVisitor();
    }

    public void produceScopeTree(CompilationUnit ast, ScopeTree scopes) {
        this.scopeTreeSkeletonMakerVisitor.visit(ast, scopes);
    }
}
