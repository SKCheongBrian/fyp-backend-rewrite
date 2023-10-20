package com.cheong.brian.javadiagrambackend.web.controllers;

import com.cheong.brian.javadiagrambackend.compiler.AstProcessor;
import com.cheong.brian.javadiagrambackend.compiler.scope_tree.scopes.ScopeTree;
import com.cheong.brian.javadiagrambackend.web.json_objects.ProgramString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private AstProcessor astProcessor;

    @PostMapping(path = "/test")
    public ResponseEntity<String> test(@RequestBody ProgramString program) throws JsonProcessingException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(program.getProgram());
        ScopeTree scopes = new ScopeTree();
        astProcessor.process(compilationUnit, scopes);
        return ResponseEntity.ok("ok");
    }

    @ExceptionHandler(ParseProblemException.class)
    public ResponseEntity<String> handleParseException(ParseProblemException ex) {
        return ResponseEntity.ok(ex.getMessage());
    }
}
