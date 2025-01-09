package com.mellenamongush.challengeLiterAlura.repository;

import com.mellenamongush.challengeLiterAlura.modelos.Autor;
import com.mellenamongush.challengeLiterAlura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anoSeleccionado AND a.fechaDeMuerte >= :anoSeleccionado")
    List<Autor> buscarAutorVivoEnDeterminadaFecha(int anoSeleccionado);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDeDescargas DESC")
    List<Libro> top10MasDescargados();

    List<Autor> findByNombreContainingIgnoreCase(String nombre);
}
