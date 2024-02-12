package com.cheong.brian.javadiagrambackend.web.controllers;

import com.cheong.brian.javadiagrambackend.payload.ProgramData;
import com.cheong.brian.javadiagrambackend.source_processor.SourceProcessor;
import com.cheong.brian.javadiagrambackend.web.json_objects.ProgramString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javaparser.ParseProblemException;
import com.google.gson.Gson;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A REST API for testing.
 */
@RestController
@CrossOrigin(origins = "*")
public class TestController {
    /**
     * Tests to do whatever brian needs at the given point of time. :)
     *
     * @param program The ProgramString from the frontend.
     * @return a String to say ok.
     * @throws JsonProcessingException If there's an issue that I don't understand yet.
     */
    @PostMapping(path = "/test")
    public ResponseEntity<String> test(@RequestBody ProgramString program) throws JsonProcessingException {
        ProgramData programData = SourceProcessor.processProgram(program.getProgram());
        Gson gson = new Gson();
        String json = gson.toJson(programData);

        return ResponseEntity.ok(json);
    }

    /**
     * Supposed to handle exceptions?
     *
     * @param ex The exception.
     * @return A String that contains the exception message.
     */
    @ExceptionHandler(ParseProblemException.class)
    public ResponseEntity<String> handleParseException(ParseProblemException ex) {
        return ResponseEntity.ok(ex.getMessage());
    }
}
