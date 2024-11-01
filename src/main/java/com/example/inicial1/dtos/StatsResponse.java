package com.example.inicial1.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    private long countMutantDna;

    @JsonProperty("count_human_dna")
    private long countHumanDna;

    private double ratio;
}