package com.mellenamongush.challengeLiterAlura.principal;

import com.mellenamongush.challengeLiterAlura.datos.DatosAutor;
import com.mellenamongush.challengeLiterAlura.datos.DatosGenerales;
import com.mellenamongush.challengeLiterAlura.datos.DatosLibros;
import com.mellenamongush.challengeLiterAlura.modelos.Autor;
import com.mellenamongush.challengeLiterAlura.modelos.Libro;
import com.mellenamongush.challengeLiterAlura.repository.AutorRepository;
import com.mellenamongush.challengeLiterAlura.repository.LibroRepository;
import com.mellenamongush.challengeLiterAlura.servicios.ConsumoAPI;
import com.mellenamongush.challengeLiterAlura.servicios.ConvierteDatos;
import com.mellenamongush.challengeLiterAlura.servicios.LibroService;
import org.antlr.v4.runtime.InputMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    @Autowired
    private final LibroRepository libroRepository;
    @Autowired
    private final AutorRepository autorRepository;
    @Autowired
    private LibroService libroService;

    private List<Libro> libros;
    private DatosAutor autores;
    private DatosLibros datosLibros;

    private Optional<Autor> autorEncontrado;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() {
        var json = consumoApi.obtenerDatos(URL_BASE);
        var opcion = -1;
        var menu = """
                *******************************************
                1 - Buscar Libro por titulo en WEB
                2 - Listar libros por titulo en base de datos
                3 - Listar libros por autor en base de datos
                4 - Listar Autores por determinado anho
                5 - Listar libros por idioma
                6 - Top 10 Libros mas descargados
                7 - Buscar autor de base de datos
                8 - Buscar libro por titulo ne base de datos
                
                0 - Salir
                *******************************************;
                """;
        while (opcion != 0) {
            System.out.println("\n*******************************************");
            System.out.println("\nELIGE UNA DE LAS OPCIONES DEL MENU: \n");
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error en el ingreso de datos, ingrese numero del Menu");

            } finally {
                teclado.nextLine();
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorNombre();
                    break;
                case 2:
                    listarLibrosRegistradosEnBaseDeDatos();
                    break;
                case 3:
                    buscarLibroPorNombreDelAutor();
                    break;
                case 4:
                    listarAutoresPorDeterminadoAnho();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    top10MasDescargados();
                    break;
                case 7:
                    buscarAutorPorNombre();
                    break;
                case 8:
                    buscarLibroPorTituloEnBaseDeDatos();
                    break;
                case 0:
                    System.out.println("Serrando la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private DatosLibros getDatosLibro() {
        System.out.println("*******************************************");
        System.out.println("\nESCRIBE EL TITULO DEL LIBRO QUE DESEAS BUSCAR: ");
        var nombreDelLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos((URL_BASE + "?search=" + nombreDelLibro.replace(" ", "+")));
        var datosBusqueda = conversor.obtenerDatos(json, DatosGenerales.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreDelLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("\n*******************************************");
            System.out.println("LIBRO ENCONTRADO! ");
            return libroBuscado.get();
        } else {
            System.out.println("\n*******************************************");
            System.out.println("LIBRO NO ENCONTRADO ");
            return null;
        }
    }

    private void buscarLibroPorNombre() {
        DatosLibros datos = getDatosLibro();
        if (datos != null) {
            if (!libroRepository.existsByTitulo(datos.titulo())) {
                try {
                    Autor autor = new Autor(datos.autores().get(0));
                    Autor autorExistente = autorRepository.findByNombre(autor.getNombre());

                    if (autorExistente == null) {
                        autor = autorRepository.save(autor);
                    } else {
                        autor = autorExistente;
                    }
                    Libro libros = new Libro(datos, autor);
                    libroRepository.save(libros);
                    System.out.println("\n*******************************************");
                    System.out.println("LIBRO GUARDADO EXITOSAMENTE EN BASE DE DATOS: \n" + datos);
                } catch (Exception e) {
                    System.out.println("\n*******************************************");
                    System.err.println("Error al guardar libro\n" + datos);
                }
            } else {
                System.out.println("*******************************************");
                System.out.println("El libro '" + datos.titulo() + "' ya existe en la base de datos.");
            }
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("\n*******************************************");
        System.out.println("ESCRIBE EL NOMBRE DEL AUTOR QUE DESEAS BUSCAR: ");
        String nombreDelAutor = teclado.nextLine();

        // Buscar autores cuyo nombre contenga el texto ingresado
        List<Autor> autoresEncontrados = autorRepository.findByNombreContainingIgnoreCase(nombreDelAutor);

        if (!autoresEncontrados.isEmpty()) {
            System.out.println("\nAUTORES ENCONTRADOS:");
            autoresEncontrados.forEach(autor -> {
                System.out.println("NOMBRE DEL AUTOR: " + autor.getNombre() +
                        ", FECHA DE NACIMIENTO: " + autor.getFechaDeNacimiento() +
                        ", FECHA DE FALLECIMIENTO: " + autor.getFechaDeMuerte());
            });
        } else {
            System.out.println("\nNO SE ENCONTRARON AUTORES CON ESE NOMBRE.");
        }
    }

    private void listarLibrosRegistradosEnBaseDeDatos() {
        List<Libro> libros = libroRepository.findAll();
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(l -> System.out.println("TITULO: " + l.getTitulo() + ", AUTOR: " + l.getAutor().getNombre() + ", IDIOMA: " + l.getIdioma() + ", NUMERO DE DESCARGAS: " + l.getNumeroDeDescargas()));
    }

    private void buscarLibroPorNombreDelAutor() {
        List<Autor> autores = autorRepository.findAll();

        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(a -> System.out.println("NOMBRE DEL AUTOR: " + a.getNombre()
                        + ", FECHA DE NACIMIENTO: " + a.getFechaDeNacimiento()
                        + ", FECHA DE FALLECIMIENTO: " + a.getFechaDeMuerte()));
    }

    private void buscarLibroPorTituloEnBaseDeDatos() {
        System.out.println("\n*******************************************");
        System.out.println("ESCRIBE EL TITULO DEL LIBRO QUE DESEAS BUSCAR: ");
        String tituloDelLibro = teclado.nextLine();

        // Buscar libros cuyo título contenga el texto ingresado
        List<Libro> librosEncontrados = libroRepository.findByTituloContainingIgnoreCase(tituloDelLibro);

        if (!librosEncontrados.isEmpty()) {
            System.out.println("\nLIBROS ENCONTRADOS:");
            librosEncontrados.forEach(libro -> {
                System.out.println("TITULO: " + libro.getTitulo() +
                        ", AUTOR: " + libro.getAutor().getNombre() +
                        ", IDIOMA: " + libro.getIdioma() +
                        ", NUMERO DE DESCARGAS: " + libro.getNumeroDeDescargas());
            });
        } else {
            System.out.println("\nNO SE ENCONTRARON LIBROS CON ESE TITULO.");
        }
    }

    private void listarAutoresPorDeterminadoAnho() {
        int anoSeleccionado;
        do {
            System.out.println("ESCRIBE EL ANHO APROXIMADO QUE VIVIO AUTOR:");
            while (!teclado.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, ingresa el anho.");
                teclado.next();
            }
            anoSeleccionado = teclado.nextInt();
            teclado.nextLine();

            if (String.valueOf(anoSeleccionado).length() != 4) {
                System.out.println("El anho debe tener cuatro dígitos. Intenta de nuevo.");
            }
        } while (String.valueOf(anoSeleccionado).length() != 4);

        System.out.println("Anho seleccionado: " + anoSeleccionado);
        List<Autor> filtroAutores = autorRepository.buscarAutorVivoEnDeterminadaFecha(anoSeleccionado);
        filtroAutores.forEach(a ->
                System.out.println("NOMBRE DEL AUTOR: " + a.getNombre() + ", FECHA DE NACIMIENTO: " + a.getFechaDeNacimiento() + ", FECHA DE FALLECIMIENTO: " + a.getFechaDeMuerte()));
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                ELIGE IDOMA DEL LIBRO QUE DESEA BUSCAR:
                es- ESPANOL
                en- INGLES
                fr- FRANCES
                pt-PORTUGUES
                """);

        var idiomaSeleccionado = teclado.nextLine().toLowerCase();
        if (idiomaSeleccionado.equals("en") || idiomaSeleccionado.equals("es") || idiomaSeleccionado.equals("fr") || idiomaSeleccionado.equals("pt")) {
            long cantidad = libroService.countByIdioma(idiomaSeleccionado);
            System.out.println("CANTIDAD DE LIBROS EN ESTA IDIOMA EN BASE DE DATOS: " + cantidad);
        } else {
            System.out.println("Escribe una opción correcta del idioma");
            return;
        }

        List<Libro> librosPorIdioma = libroService.findByIdioma(idiomaSeleccionado);
        librosPorIdioma.forEach(l ->
                System.out.println("TITULO: " + l.getTitulo() + ", AUTOR: " + l.getAutor().getNombre() + ", IDIOMA: " + l.getIdioma() + ", NUMERO DE DESCARGAS: " + l.getNumeroDeDescargas()));
    }

    private void top10MasDescargados() {
        System.out.println("TOP 10 LIBROS MAS DESCARGADOS:\n");
        var datosBusqueda = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(datosBusqueda, DatosGenerales.class);
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
    }
}








