package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import org.springframework.cglib.core.Block;

import java.util.Optional;
import java.util.stream.Stream;

import static com.github.javaparser.ast.Modifier.Keyword.PUBLIC;

public class ConstructorSuperAdderVisitor extends ModifierVisitor<Void> {
    @Override
    public ConstructorDeclaration visit(ConstructorDeclaration c, Void args) {
        BlockStmt body = c.getBody();
        c.getName();
        if (!hasSuperInvocation(body)) {
            addSuperInvocation(body);
        }
        return c;
    }

    private void addSuperInvocation(BlockStmt body) {
        NodeList<Statement> statements = body.getStatements();
        Statement superInvocation = new ExplicitConstructorInvocationStmt(false, null, new NodeList<Expression>());
        statements.add(0, superInvocation);
    }

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
