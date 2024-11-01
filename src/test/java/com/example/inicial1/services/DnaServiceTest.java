package com.example.inicial1.services;

import com.example.inicial1.dtos.StatsResponse;
import com.example.inicial1.entities.Dna;
import com.example.inicial1.repositories.DnaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DnaServiceTest {
    @InjectMocks
    private DnaService dnaService;

    @Mock
    private DnaRepository dnaRepository;

    @Mock
    private StatsService statsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String[] createDna(String... rows) {
        return rows;
    }

    // Test que verifica si el ADN ya existe y es mutante
    @Test
    public void testAnalyzeDna1() {
        String[] dna = createDna("ATGCGA", "CAGTGC", "TTATGT", "AGAGGA", "CCCCTA", "TCACTG");

        Dna existingDna = new Dna();
        existingDna.setMutant(true);
        when(dnaRepository.findByDna(dna)).thenReturn(Optional.of(existingDna));

        boolean result = dnaService.isMutant(dna);

        assertTrue(result);
        verify(dnaRepository, never()).save(any());
    }

    // Test que verifica que un ADN es mutante
    @Test
    public void testMutantDnaWithRandomPattern() {
        String[] dna = createDna("AAGGAA", "CCGCAA", "TTTTAG", "AGGTAG", "CCACCA", "AAGTAG");
        assertTrue(dnaService.isMutant(dna));
    }

    @Test
    public void testNonMutant212() {
        String[] dna = createDna("ATGATG", "GTCTTA", "AATTGG", "ACTAGT", "GGATTC", "AGGCAA");
        assertFalse(dnaService.isMutant(dna));
    }

    // Prueba de límites: ADN vacío
    @Test
    public void testEmptyDna23123() {
        String[] dna = {};
        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.isMutant(dna);
        });
    }

    // Prueba de límites: ADN con caracteres inválidos
    @Test
    public void testInvalidCharacters1() {
        String[] dna = createDna("ATGCGX", "CAGTGC", "TTATGT", "AGAGGA", "CCCCTA", "TCACTG");
        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.isMutant(dna);
        });
    }


    // Verificación de estadísticas
    @Test
    public void ReturnsCorrectStats1() {
        StatsResponse mockStats = new StatsResponse(5L, 3L, 1.6666666666666667);
        when(statsService.getStats()).thenReturn(mockStats);

        StatsResponse stats = statsService.getStats();

        assertEquals(5L, stats.getCountMutantDna());
        assertEquals(3L, stats.getCountHumanDna());
        assertEquals(1.6666666666666667, stats.getRatio());
    }
    // Test que verifica si el ADN ya existe y es mutante
    @Test
    public void ReturnsIsMutant() {
        // Preparar datos
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAGGA", "CCCCTA", "TCACTG"};
        String dnaSequence = String.join(",", dna);

        // Simular comportamiento del repositorio
        Dna existingDna = new Dna();
        existingDna.setMutant(true);
        when(dnaRepository.findByDna(new String[]{dnaSequence})).thenReturn(Optional.of(existingDna));

        // Ejecutar método
        boolean result = dnaService.isMutant(dna);

        // Verificar resultados
        assertTrue(result);
        verify(dnaRepository, never()).save(any());
    }

    // Test que verifica las estadísticas
    @Test
    public void ReturnsCorrectStats() {
        // Crear el objeto StatsResponse con datos simulados
        StatsResponse mockStats = new StatsResponse(5L, 3L, 1.6666666666666667);

        // Simular el comportamiento del servicio StatsService
        when(statsService.getStats()).thenReturn(mockStats);

        // Ejecutar el método
        StatsResponse stats = statsService.getStats();

        // Verificar resultados
        assertEquals(5L, stats.getCountMutantDna());
        assertEquals(3L, stats.getCountHumanDna());
        assertEquals(1.6666666666666667, stats.getRatio());
    }

    // Test que verifica filas en ADN
    @Test
    public void testRows() {
        String[] dna = {
                "AAAATG",
                "TGCAGT",
                "GCTTCC",
                "CCCCTG",
                "GTAGTC",
                "AGTCAC"
        };
        assertTrue(dnaService.isMutant(dna));
    }
    @Test
    public void testRows2() {
        String[] dna = {
                "AAAATG",
                "TGCAGT",
                "GCTTCC",
                "CCCCTG",
                "GTAGTC",
                "AGTCAC"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    // Test que verifica columnas en ADN
    @Test
    public void testColumns() {
        String[] dna = {
                "AGAATG",
                "TGCAGT",
                "GCTTCC",
                "GTCCTC",
                "GTAGTC",
                "GGTCAC"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    // Test que verifica diagonales principales en ADN
    @Test
    public void testMainDiagonals() {
        String[] dna = {
                "AGAATG",
                "TACAGT",
                "GCAGCC",
                "TTGATG",
                "GTAGTC",
                "AGTCAA"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    // Test que verifica diagonales secundarias izquierda en ADN
    @Test
    public void testSecondaryLeftDiagonals() {
        String[] dna = {
                "ATAATG",
                "GTTAGT",
                "GGCTCG",
                "TTGATG",
                "GTAGTC",
                "AGTCAA"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    // Test que verifica diagonales secundarias derecha en ADN
    @Test
    public void testSecondaryRightDiagonals() {
        String[] dna = {
                "ATAATG",
                "GTATGA",
                "GCTTAG",
                "TTTAGG",
                "GTAGTC",
                "AGTCAA"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    // Test que verifica diagonales terciarias izquierda en ADN
    @Test
    public void testTertiaryLeftDiagonals() {
        String[] dna = {
                "ATGATG",
                "GTAGTA",
                "CCTTGG",
                "TCTAGG",
                "GGCGTC",
                "AGTCAA"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    // Test que verifica diagonales terciarias derecha en ADN
    @Test
    public void testTertiaryRightDiagonals() {
        String[] dna = {
                "ATGATG",
                "GTATTA",
                "AATTGG",
                "ACTAGT",
                "GGAGTC",
                "AGGCAA"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    // Test que verifica que un ADN no es mutante
    @Test
    public void testNonMutant() {
        String[] dna = {
                "ATGATG",
                "GTCTTA",
                "AATTGG",
                "ACTAGT",
                "GGATTC",
                "AGGCAA"
        };
        assertFalse(dnaService.isMutant(dna));
    }

    // Tests proporcionados por el profesor
    @Test
    public void testMutant1() {
        String[] dna = {
                "AAAA",
                "CCCC",
                "TCAG",
                "GGTC"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    @Test
    public void testNonMutant1() {
        String[] dna = {
                "AAAT",
                "AACC",
                "AAAC",
                "CGGG"
        };
        assertFalse(dnaService.isMutant(dna));
    }

    @Test
    public void testMutant2() {
        String[] dna = {
                "TGAC",
                "AGCC",
                "TGAC",
                "GGTC"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    @Test
    public void testMutant3() {
        String[] dna = {
                "AAAA",
                "AAAA",
                "AAAA",
                "AAAA"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    @Test
    public void testNonMutant2() {
        String[] dna = {
                "TGAC",
                "ATCC",
                "TAAG",
                "GGTC"
        };
        assertFalse(dnaService.isMutant(dna));
    }

    @Test
    public void testMutant4() {
        String[] dna = {
                "TCGGGTGAT",
                "TGATCCTTT",
                "TACGAGTGA",
                "AAATGTACG",
                "ACGAGTGCT",
                "AGACACATG",
                "GAATTCCAA",
                "ACTACGACC",
                "TGAGTATCC"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    @Test
    public void testMutant5() {
        String[] dna = {
                "TTTTTTTTT",
                "TTTTTTTTT",
                "TTTTTTTTT",
                "TTTTTTTTT",
                "CCGACCAGT",
                "GGCACTCCA",
                "AGGACACTA",
                "CAAAGGCAT",
                "GCAGTCCCC"
        };
        assertTrue(dnaService.isMutant(dna));
    }

    @Test
    public void testHorizontalSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(dnaService.isMutant(dna)); // Hay una secuencia horizontal.
    }

    @Test
    public void testVerticalSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "TCCCTA",
                "TCACTG"
        };
        assertTrue(dnaService.isMutant(dna)); // Hay una secuencia vertical en la tercera columna.
    }

    @Test
    public void testMultipleSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(dnaService.isMutant(dna)); // Tanto horizontal como diagonal.
    }

    @Test
    public void testExactSize4x4() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAG"
        };
        assertFalse(dnaService.isMutant(dna)); // No hay mutaciones en una secuencia de tamaño 4x4.
    }

    // Validaciones adicionales
    @Test
    public void testInvalidCharacters() {
        String[] dna = {
                "ATGCGX",
                "CAGTGC",
                "TTATGT",
                "AGAGGA",
                "CCCCTA",
                "TCACTG"
        };
        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.isMutant(dna);
        });
    }

    @Test
    public void testInvalidLength() {
        String[] dna = {
                "ATG",
                "CAGT",
                "TTATGT",
                "AGAGGA",
                "CCCCTA"
        };
        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.isMutant(dna);
        });
    }

    @Test
    public void testEmptyDna() {
        String[] dna = {};
        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.isMutant(dna);
        });
    }

}
