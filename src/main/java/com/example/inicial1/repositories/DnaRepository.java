package com.example.inicial1.repositories;

import com.example.inicial1.entities.Dna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRepository extends JpaRepository<Dna,Long> {

    Optional<Dna> findByDna(String[] dnaSequence);

    // Contar cuántas secuencias son mutantes
    long countByIsMutant(boolean isMutant);

}
