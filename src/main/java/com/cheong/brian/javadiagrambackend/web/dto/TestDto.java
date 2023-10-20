package com.cheong.brian.javadiagrambackend.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.javaparser.ast.CompilationUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    @JsonIgnore
    private CompilationUnit compilationUnit;
}
