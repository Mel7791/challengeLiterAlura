package com.mellenamongush.challengeLiterAlura.repository;

import com.mellenamongush.challengeLiterAlura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByTitulo(String titulo);

    List<Libro> findByIdioma(String idiomaSeleccionado);

    long countByIdioma(String idiomaSeleccionado);

    List<Libro> findByTituloContainingIgnoreCase(String titulo);
}
