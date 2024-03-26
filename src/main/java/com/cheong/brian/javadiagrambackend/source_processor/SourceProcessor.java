package com.cheong.brian.javadiagrambackend.source_processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.cheong.brian.javadiagrambackend.debugger.Debugger;
import com.cheong.brian.javadiagrambackend.payload.ProgramData;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class SourceProcessor {
    public static ProgramData processProgram(String programString) {
        String mainClassName = getMainClassName(programString);
        if (mainClassName == null) {
            String errorMessage = "Please ensure that your class has a main method.";
            return ProgramData.failure(errorMessage);
        }
        String sandBoxPath = "./sandbox/";
        String mainClassFileName = mainClassName + ".java";
        new File(sandBoxPath).mkdirs();
        String javaSourcePath = sandBoxPath + mainClassFileName;
        writeJavaFile(javaSourcePath, programString);
        return compile(javaSourcePath, mainClassName);
    }

    private static ProgramData compile(String javaSourcePath, String mainClassName) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjects(javaSourcePath);
        ProgramData result = null;

        if (compiler.getTask(null, fileManager, diagnosticCollector, Arrays.asList("-g"), null, compilationUnit)
                .call()) {
            JavaFileObject j = compilationUnit.iterator().next();
            try {
                result = new Debugger(mainClassName).stepThroughClass();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Print compilation errors
            System.err.println("Compilation errors:");
            StringBuilder errorBuilder = new StringBuilder("Compilation errors:");
            for (var diagnostic : diagnosticCollector.getDiagnostics()) {
                System.err.println(diagnostic.getMessage(null));
                errorBuilder.append(diagnostic.getMessage(null));
            }
            return ProgramData.failure(errorBuilder.toString());
        }
        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void writeJavaFile(String javaSourcePath, String programString) {
        try {
            FileWriter fileWriter = new FileWriter(javaSourcePath);
            fileWriter.write(programString);
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("An error has occurred");
            e.printStackTrace();
        }
    }

    private static String getMainClassName(String programString) {
        CompilationUnit cu;
        try {
            cu = StaticJavaParser.parse(programString);
        } catch (ParseProblemException e) {
            return null;
        }
        Optional<MethodDeclaration> mainMethod = cu.findFirst(MethodDeclaration.class,
                md -> md.getNameAsString().equals("main") && md.isPublic() && md.isStatic());
        if (mainMethod.isPresent()) {
            ClassOrInterfaceDeclaration mainClass = (ClassOrInterfaceDeclaration) mainMethod.get().getParentNode()
                    .get();
            return mainClass.getNameAsString();
        }
        return null;
    }
}
