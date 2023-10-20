package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.scope_constructors;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.ScopeTree;
import com.github.javaparser.ast.CompilationUnit;

/**
 * This class is responsible for creating a ScopeTree given an AST.
 */
public class ScopeTreeProducer {
    private final ScopeTreeSkeletonMakerVisitor scopeTreeSkeletonMakerVisitor;

    /**
     * Constructor for the ScopeTreeProducer.
     */
    public ScopeTreeProducer() {
        this.scopeTreeSkeletonMakerVisitor = new ScopeTreeSkeletonMakerVisitor();
    }

    /**
     * Produces the scope tree given an ast.
     * @param ast the AST that the ScopeTree will be produced from.
     * @param scopes the ScopeTree that is modified according to the AST.
     */
    public void produceScopeTree(CompilationUnit ast, ScopeTree scopes) {
        this.scopeTreeSkeletonMakerVisitor.visit(ast, scopes);
    }
}
