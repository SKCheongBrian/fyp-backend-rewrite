package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.scope_constructors;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.ClassScope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.MethodScope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.Scope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Field;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Variable;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;


/**
 * This visitor is responsible for populating the scopes with its fields and variables depending on the scope type.
 */
public class PopulateScopeTreeVisitor extends VoidVisitorAdapter<Scope> {
    /**
     * Visits the AST and sets the correct scope.
     *
     * @param cu      The AST.
     * @param current The ScopeTree.
     */
    @Override
    public void visit(CompilationUnit cu, Scope current) {
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration<?> type : types) {
            if (!type.isClassOrInterfaceDeclaration()) {
                continue;
            }
            Scope classScope = current.getChild(type.getName());
            visit(type.asClassOrInterfaceDeclaration(), classScope);
        }
    }

    /**
     * Adds any fields to the ClassScope.
     *
     * @param cd      The class declaration.
     * @param current The current ClassScope.
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Scope current) {
        // Do nothing if it is an interface.
        if (cd.isInterface()) {
            return;
        }

        ClassScope currentClassScope = current.asClassScope();
        List<FieldDeclaration> fields = cd.getFields();
        for (FieldDeclaration fieldDeclaration : fields) {
            // Only supposed to have one declaration fragment.
            Field field = new Field(fieldDeclaration.getVariable(0).getName(), fieldDeclaration.isStatic());
            currentClassScope.addField(field);
        }

        List<MethodDeclaration> methods = cd.getMethods();
        for (MethodDeclaration m : methods) {
            SimpleName name = m.getName();
            Scope methodScope = currentClassScope.getChild(name);
            visit(m, methodScope);
        }

        for (BodyDeclaration<?> member : cd.getMembers()) {
            if (member.isClassOrInterfaceDeclaration()) {
                ClassOrInterfaceDeclaration type = (ClassOrInterfaceDeclaration) member;
                Scope classScope = currentClassScope.getChild(type.getName());
                visit(type, classScope);
            }
        }
    }

    /**
     * Adds any variable declarations in the method declaration.
     *
     * @param md      The method declaration.
     * @param current The MethodScope that corresponds to the method declaration.
     */
    @Override
    public void visit(MethodDeclaration md, Scope current) {
        Optional<BlockStmt> optionalBody = md.getBody();
        if (optionalBody.isEmpty()) {
            return;
        }
        MethodScope currentMethodScope = current.asMethodScope();
        BlockStmt blockStmt = optionalBody.get();
        for (Statement stmt : blockStmt.getStatements()) {
            if (stmt.isExpressionStmt() && stmt.asExpressionStmt().getExpression().isVariableDeclarationExpr()) {
                VariableDeclarationExpr variableDeclarationExpr = stmt.asExpressionStmt().getExpression().asVariableDeclarationExpr();
                VariableDeclarator variableDeclarator = variableDeclarationExpr.getVariable(0);
                Variable variable = new Variable(variableDeclarator.getName(), false);
                currentMethodScope.addVariable(variable);
            }

            if (stmt.isLocalClassDeclarationStmt()) {
                ClassOrInterfaceDeclaration type = stmt.asLocalClassDeclarationStmt().getClassDeclaration();
                Scope classScope = current.getChild(type.getName());
                visit(type, classScope);
            }
        }
    }
}