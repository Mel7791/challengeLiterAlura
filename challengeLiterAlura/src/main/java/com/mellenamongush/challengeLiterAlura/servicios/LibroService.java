package com.mellenamongush.challengeLiterAlura.servicios;

import com.mellenamongush.challengeLiterAlura.datos.DatosLibros;
import com.mellenamongush.challengeLiterAlura.modelos.Autor;
import com.mellenamongush.challengeLiterAlura.modelos.Libro;
import com.mellenamongush.challengeLiterAlura.repository.AutorRepository;
import com.mellenamongush.challengeLiterAlura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    public LibroService(AutorRepository autorRepository, LibroRepository libroRepository){
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void guardarLibro(DatosLibros datos) {
        // Busca el autor en la base de datos. Si no existe, lo crea.
        Autor autor = autorRepository.findByNombre(datos.autores().get(0).nombre());
        if (autor == null) {
            autor = new Autor(datos.autores().get(0));
            autor = autorRepository.save(autor);
        }

        Libro libros = new Libro(); // Pasa el autor al constructor de Libros
        libroRepository.save(libros);
    }

    public long countByIdioma(String idiomaSeleccionado) {
        return libroRepository.countByIdioma(idiomaSeleccionado);
    }

    public List<Libro> findByIdioma(String idiomaSeleccionado) {
        return libroRepository.findByIdioma(idiomaSeleccionado);
    }
}
