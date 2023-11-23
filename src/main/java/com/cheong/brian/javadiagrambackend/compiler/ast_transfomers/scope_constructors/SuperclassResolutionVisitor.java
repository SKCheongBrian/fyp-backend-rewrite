package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.scope_constructors;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.ClassScope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.MethodScope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.Scope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.variables.Variable;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class SuperclassResolutionVisitor extends VoidVisitorAdapter<Scope> {
    /**
     * Visits the AST and sets the correct scope.
     *
     * @param cu      The AST
     * @param current The ScopeTree
     */
    @Override
    public void visit(CompilationUnit cu, Scope current) {
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration<?> type : types) {
            if (!type.isClassOrInterfaceDeclaration()) {
                continue;
            }
            Scope classScope = current.getChild(type.getName().asString());
            visit(type.asClassOrInterfaceDeclaration(), classScope);
        }
    }

    /**
     * Visit the class declaration and see if there is any superclasses,
     * if there is find the super scope and add the super scope to the current scope.
     *
     * @param cd      The class declaration
     * @param current The scope of the class declaration
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Scope current) {
        // Do nothing if it is an interface.
        if (cd.isInterface()) {
            return;
        }
        ClassScope currentClassScope = current.asClassScope();
        // if there is a super class, set the super scope
        if (!cd.getExtendedTypes().isEmpty()) {
            String superClass = cd.getExtendedTypes().get(0).getNameAsString();
            Scope scopePointer = currentClassScope.getParent();
            while (scopePointer != null) {
                for (String child : scopePointer.getChildren().keySet()) {
                    if (classScopeMatchesSuperName(scopePointer.getChild(child), superClass)) {
                        currentClassScope.addSuperScope(scopePointer.getChild(child).asClassScope());
                        break;
                    }
                }
                scopePointer = scopePointer.getParent();
            }
        }

        List<MethodDeclaration> methods = cd.getMethods();
        for (MethodDeclaration m : methods) {
            String name = m.getName().asString();
            Scope methodScope = currentClassScope.getChild(name);
            visit(m, methodScope);
        }

        for (BodyDeclaration<?> member : cd.getMembers()) {
            if (member.isClassOrInterfaceDeclaration()) {
                ClassOrInterfaceDeclaration type = (ClassOrInterfaceDeclaration) member;
                Scope classScope = currentClassScope.getChild(type.getFullyQualifiedName()
                        .orElseGet(() -> type.getNameAsString()));
                visit(type, classScope);
            }
        }
    }

    /**
     * Visit the method declaration and continue visiting any nested classes
     * within the method declaration.
     * @param md The Method Declaration.
     * @param current The scope of the method declaration.
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
            if (stmt.isLocalClassDeclarationStmt()) {
                ClassOrInterfaceDeclaration type = stmt.asLocalClassDeclarationStmt().getClassDeclaration();
                Scope classScope = current.getChild(type.getFullyQualifiedName()
                        .orElseGet(() -> type.getNameAsString()));
                visit(type, classScope);
            }
        }
    }

    private boolean classScopeMatchesSuperName(Scope scopePointer, String other) {
        if (!scopePointer.isClassScope()) {
            return false;
        }
        ClassScope classScopePointer = scopePointer.asClassScope();
        return classScopePointer.matchesUnambiguousName(other);
    }
}
