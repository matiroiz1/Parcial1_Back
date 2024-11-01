package com.example.inicial1.controllers;

import com.example.inicial1.entities.Dna;
import com.example.inicial1.repositories.DnaRepository;
import com.example.inicial1.services.DnaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DnaControllerTest {

    @Mock
    private DnaService dnaService;

    @Mock
    private DnaRepository dnaRepository;

    @InjectMocks
    private DnaController dnaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkMutant_whenDnaExistsInRepository_shouldReturnStoredResult() {
        // Arrange
        String[] dnaSequence = {"ATCG", "CAGT", "TTAT", "AGAA"};
        Dna dna = new Dna();
        dna.setDna(dnaSequence);
        dna.setMutant(true);

        when(dnaRepository.findByDna(dnaSequence)).thenReturn(Optional.of(dna));

        // Act
        ResponseEntity<Boolean> response = dnaController.checkMutant(dna);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(dnaRepository, times(1)).findByDna(dnaSequence);
        verify(dnaService, never()).isMutant(any());
    }

    @Test
    void checkMutant_whenDnaDoesNotExistAndIsMutant_shouldReturnOkAndSaveDna() {
        // Arrange
        String[] dnaSequence = {"ATCG", "CAGT", "TTAT", "AGAA"};
        Dna dna = new Dna();
        dna.setDna(dnaSequence);

        when(dnaRepository.findByDna(dnaSequence)).thenReturn(Optional.empty());
        when(dnaService.isMutant(dnaSequence)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = dnaController.checkMutant(dna);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(dnaService, times(1)).isMutant(dnaSequence);
        verify(dnaRepository, times(1)).save(dna);
    }

    @Test
    void checkMutant_whenDnaDoesNotExistAndIsNotMutant_shouldReturnForbiddenAndSaveDna() {
        // Arrange
        String[] dnaSequence = {"ATCG", "CAGT", "TTGT", "AGAA"};
        Dna dna = new Dna();
        dna.setDna(dnaSequence);

        when(dnaRepository.findByDna(dnaSequence)).thenReturn(Optional.empty());
        when(dnaService.isMutant(dnaSequence)).thenReturn(false);

        // Act
        ResponseEntity<Boolean> response = dnaController.checkMutant(dna);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(dnaService, times(1)).isMutant(dnaSequence);
        verify(dnaRepository, times(1)).save(dna);
    }
}
