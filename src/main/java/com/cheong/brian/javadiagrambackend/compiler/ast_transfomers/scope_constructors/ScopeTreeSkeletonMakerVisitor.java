package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers.scope_constructors;

import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.ClassScope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.MethodScope;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.Scope;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

/**
 * This visitor traverses the AST and creates the "skeleton" of the scopes.
 */
public class ScopeTreeSkeletonMakerVisitor extends VoidVisitorAdapter<Scope> {
    @Override
    public void visit(CompilationUnit cu, Scope current) {
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration<?> type : types) {
            if (!type.isClassOrInterfaceDeclaration()) {
                continue;
            }
            ClassScope classScope = new ClassScope(type.getName(), current, type.isStatic());
            current.addChild(type.getName(), classScope);
            // ok to cast because protected by guard
            visit((ClassOrInterfaceDeclaration) type, classScope);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration cd, Scope current) {
        if (cd.isInterface()) {
            return;
        }
        List<MethodDeclaration> methods = cd.getMethods();
        for (MethodDeclaration m : methods) {
            SimpleName name = m.getName();
            boolean isStatic = m.isStatic();
            Type returnType = m.getType();
            NodeList<TypeParameter> typeParameters = m.getTypeParameters();
            NodeList<Modifier> modifiers = m.getModifiers();
            MethodScope methodScope = new MethodScope(name, current, isStatic, returnType, typeParameters, modifiers);
            current.addChild(name, methodScope);
            visit(m, methodScope);
        }
        // Sadly, no other way of getting nested classes.
        for (BodyDeclaration<?> member : cd.getMembers()) {
            if (member.isClassOrInterfaceDeclaration()) {
                ClassOrInterfaceDeclaration type = (ClassOrInterfaceDeclaration) member;
                ClassScope classScope = new ClassScope(type.getName(), current, type.isStatic());
                current.addChild(type.getName(), classScope);
                visit(type, classScope);
            }
        }
    }

    @Override
    public void visit(MethodDeclaration md, Scope current) {
        Optional<BlockStmt> optionalBody =  md.getBody();
        if (optionalBody.isEmpty()) {
            return;
        }
        BlockStmt blockStmt = optionalBody.get();
        for (Statement stmt : blockStmt.getStatements()) {
            if (stmt.isLocalClassDeclarationStmt()) {
                ClassOrInterfaceDeclaration type = ((LocalClassDeclarationStmt) stmt).getClassDeclaration();
                ClassScope classScope = new ClassScope(type.getName(), current, type.isStatic());
                current.addChild(type.getName(), classScope);
                visit(type, classScope);
            }
        }
    }
}
