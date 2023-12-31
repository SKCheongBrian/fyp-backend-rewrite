package com.cheong.brian.javadiagrambackend.compiler.ast_transfomers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.List;

import static com.github.javaparser.ast.Modifier.Keyword.PUBLIC;

/**
 * A visitor that adds missing explicit default constructors to the AST.
 */
public class ConstructorAdderVisitor extends ModifierVisitor<Void> {
    /**
     * Visit the class declaration and adds default constructor if missing.
     * @param declaration the class declaration.
     * @param arg not used.
     * @return Modified declaration containing the missing constructor.
     */
    @Override
    public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        super.visit(declaration, arg);
        // guard clause if it's an interface.
        if (declaration.isInterface()) {
            return declaration;
        }
        List<ConstructorDeclaration> constructors = declaration.getConstructors();
        // no constructors exist so add constructor.
        if (constructors.isEmpty()) {
            declaration.addConstructor(PUBLIC);
        }
        return declaration;
    }
}
