package com.example.inicial1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Dna extends Base implements Serializable {
    private String[] dna;  // Cambiar a String[] para representar la matriz de ADN
    @JsonIgnore
    private boolean isMutant;
}
