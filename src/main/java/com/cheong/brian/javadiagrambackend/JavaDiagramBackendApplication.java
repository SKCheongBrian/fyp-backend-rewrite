package com.cheong.brian.javadiagrambackend;

import com.cheong.brian.javadiagrambackend.payload.ProgramData;
import com.cheong.brian.javadiagrambackend.source_processor.SourceProcessor;
import com.google.gson.Gson;

public class JavaDiagramBackendApplication {

    public static void main(String[] args) {
        String userProgram = args[0];
        ProgramData programData = SourceProcessor.processProgram(userProgram);
        Gson gson = new Gson();
        String json = gson.toJson(programData);
        System.out.println(json);
    }

}
