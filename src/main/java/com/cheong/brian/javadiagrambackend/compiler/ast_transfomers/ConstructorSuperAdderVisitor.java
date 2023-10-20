package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;

/**
 * Visitor that adds explicit Super constructor call if missing.
 */
public class ConstructorSuperAdderVisitor extends ModifierVisitor<Void> {
    /**
     * Visits Constructor declaration and adds Super constructor call if missing.
     * @param c The constructor declaration.
     * @param args Not used.
     * @return The modified constructor declaration.
     */
    @Override
    public ConstructorDeclaration visit(ConstructorDeclaration c, Void args) {
        BlockStmt body = c.getBody();
        c.getName();
        if (!hasSuperInvocation(body)) {
            addSuperInvocation(body);
        }
        return c;
    }

    /**
     * Adds a super constructor call to a given body.
     * @param body The body that the super constructor call will be added to.
     */
    private void addSuperInvocation(BlockStmt body) {
        NodeList<Statement> statements = body.getStatements();
        Statement superInvocation = new ExplicitConstructorInvocationStmt(false, null, new NodeList<>());
        statements.add(0, superInvocation);
    }

    /**
     * Checks if there is a super constructor call in a given body.
     * @param body The body to be checked.
     * @return True if the body has a super invocation, false otherwise.
     */
    private boolean hasSuperInvocation(BlockStmt body) {
        NodeList<Statement> statements = body.getStatements();
        for (Statement s : statements) {
            if (s.isExplicitConstructorInvocationStmt()) {
                return statements.get(0).isExplicitConstructorInvocationStmt();
            }
        }
        return false;
    }
}
