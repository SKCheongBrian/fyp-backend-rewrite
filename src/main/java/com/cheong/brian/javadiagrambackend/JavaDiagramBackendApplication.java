package com.cheong.brian.javadiagrambackend;

import com.cheong.brian.javadiagrambackend.payload.ProgramData;
import com.cheong.brian.javadiagrambackend.source_processor.SourceProcessor;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JavaDiagramBackendApplication {

    public static void main(String[] args) {
        String userProgram = args[0];
        ProgramData programData = SourceProcessor.processProgram(userProgram);
        Gson gson = new Gson();
        String json = gson.toJson(programData);
        System.out.println(json);
//        File file = new File("./content.json");
//        try {
//            PrintWriter out = new PrintWriter(file);
//            out.println(json);
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

}
