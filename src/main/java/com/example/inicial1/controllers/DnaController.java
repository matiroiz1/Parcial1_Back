package com.example.inicial1.controllers;

import com.example.inicial1.dtos.DnaResponse;
import com.example.inicial1.entities.Dna;
import com.example.inicial1.repositories.DnaRepository;
import com.example.inicial1.services.DnaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/mutant")
public class DnaController {

    private final DnaService dnaService;
    private final DnaRepository dnaRepository;

    public DnaController(DnaService dnaService, DnaRepository dnaRepository) {
        this.dnaService = dnaService;
        this.dnaRepository = dnaRepository;
    }

    @PostMapping
    public ResponseEntity<Boolean> checkMutant(@RequestBody Dna dna) {
        // Verificar si la secuencia de ADN ya está almacenada
        return dnaRepository.findByDna(dna.getDna())
                .map(existingDna -> ResponseEntity.ok(existingDna.isMutant()))
                .orElseGet(() -> {
                    // Si el ADN no fue ingresado antes, procesarlo y guardarlo
                    boolean isMutant = dnaService.isMutant(dna.getDna());
                    dna.setMutant(isMutant);
                    dnaRepository.save(dna);
                    return isMutant
                            ? ResponseEntity.ok(true)
                            : ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
                });
    }
}
