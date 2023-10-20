package com.cheong.brian.javadiagrambackend.web.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class InterpreterController {
    @PostMapping(path = "/interpreter")
    public String interpreter(@RequestBody String program) {
        return program;
    }
}
