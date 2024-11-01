package com.example.inicial1.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

enum Direction {
    ABAJO, DERECHA, DIAG_ABAJO, DIAG_ARRIBA
}

@Service
public class DnaService {

    private static final int MAX_DNA_SIZE = 1000;
    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_SEQUENCE_COUNT = 2;

    private static final Set<Character> VALID_CHARACTERS = new HashSet<>(Arrays.asList('A', 'T', 'C', 'G'));

    private final Set<String> checkedDnaSequences = new HashSet<>();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public boolean isMutant(String[] dna) {
        if (!isInputValid(dna)) return false;
        if (hasDnaBeenChecked(dna)) return false;

        boolean isMutant = findMutantSequences(dna);
        storeDnaSequence(dna);
        return isMutant;
    }

    private boolean hasDnaBeenChecked(String[] dna) {
        String dnaKey = String.join(",", dna);
        return checkedDnaSequences.contains(dnaKey);
    }

    private void storeDnaSequence(String[] dna) {
        String dnaKey = String.join(",", dna);
        checkedDnaSequences.add(dnaKey);
    }

    private boolean findMutantSequences(String[] dna) {
        int count = 0;
        int n = dna.length;

        int row = 0, col = 0;
        while (row < n && count < MUTANT_SEQUENCE_COUNT) {
            for (Direction direction : Direction.values()) {
                count += searchInDirection(dna, row, col, direction);
                if (count >= MUTANT_SEQUENCE_COUNT) return true;
            }

            col++;
            if (col == n) {
                col = 0;
                row++;
            }
        }
        return false;
    }

    private int searchInDirection(String[] dna, int row, int col, Direction direction) {
        int counter = 1;
        char prevChar = dna[row].charAt(col);
        int n = dna.length;

        while (counter < SEQUENCE_LENGTH) {
            switch (direction) {
                case DERECHA:
                    col++;
                    break;
                case ABAJO:
                    row++;
                    break;
                case DIAG_ABAJO:
                    row++;
                    col++;
                    break;
                case DIAG_ARRIBA:
                    row--;
                    col++;
                    break;
            }

            if (row < 0 || row >= n || col < 0 || col >= n) return 0;

            char curChar = dna[row].charAt(col);
            if (curChar == prevChar) {
                counter++;
            } else {
                return 0;
            }
        }
        return 1;
    }

    private boolean isInputValid(String[] dna) {
        return isInputSizeValid(dna) && isInputContentValid(dna);
    }

    private boolean isInputSizeValid(String[] dna) {
        if (dna == null || dna.length == 0 || dna.length > MAX_DNA_SIZE) {
            throw new IllegalArgumentException("El ADN no puede estar vacío o exceder el tamaño máximo.");
        }
        int nrows = dna.length;
        if (nrows < SEQUENCE_LENGTH) {
            throw new IllegalArgumentException("Longitud Incorrecta");
        }
        for (String row : dna) {
            if (row == null || row.length() != nrows) {
                throw new IllegalArgumentException("Todas las filas deben tener la misma longitud y no pueden ser nulas.");
            }
        }
        return true;
    }

    private boolean isInputContentValid(String[] dna) {
        for (String s : dna) {
            for (int k = 0; k < s.length(); k++) {
                char c = s.charAt(k);
                if (!VALID_CHARACTERS.contains(c)) {
                    throw new IllegalArgumentException("El ADN contiene caracteres inválidos: " + c);
                }
            }
        }
        return true;
    }
}
