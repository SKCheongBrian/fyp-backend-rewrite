package com.cheong.brian.javadiagrambackend.compiler;

import com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.ConstructorAdderVisitor;
import com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.ConstructorSuperAdderVisitor;
import com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.scope_constructors.ScopeTreeProducer;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.ScopeTree;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Service;

/**
 * A facade that hides the details of the processing of the AST from the client.
 */
@Service
public class AstProcessor {
    private final ConstructorAdderVisitor constructorAdderVisitor;
    private final ConstructorSuperAdderVisitor constructorSuperAdderVisitor;
    private final ScopeTreeProducer scopeTreeProducer;

    /**
     * Constructor for the AstProcessor.
     */
    public AstProcessor() {
        this.constructorAdderVisitor = new ConstructorAdderVisitor();
        this.constructorSuperAdderVisitor = new ConstructorSuperAdderVisitor();
        this.scopeTreeProducer = new ScopeTreeProducer();
    }

    /**
     * Processes the AST and augments it according to the passes.
     * @param ast The AST to be processed.
     * @param scopes The ScopeTree that will be modified according to the AST.
     */
    public void process(CompilationUnit ast, ScopeTree scopes) {
        constructorAdderVisitor.visit(ast, null);
        constructorSuperAdderVisitor.visit(ast, null);
        scopeTreeProducer.produceScopeTree(ast, scopes);
    }
}
