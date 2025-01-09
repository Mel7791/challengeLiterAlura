Challenge LiterAlura


Descripción del Proyecto

Este proyecto, Challenge LiterAlura, está diseñado para interactuar con la API de Gutendex, una plataforma que proporciona datos sobre libros digitales, con el objetivo de crear una base de datos enriquecida que permita:

Buscar libros por título.

Listar libros y autores almacenados en una base de datos.

Filtrar libros por idioma y listar los más descargados.

Consultar información sobre autores.

Tecnologías Utilizadas

Java 17

Spring Boot: Para la configuración del proyecto y la gestión de dependencias.

Maven: Herramienta de construcción del proyecto.

Gutendex API: Fuente de datos para los libros.

JPA: Para la persistencia y gestión de datos en la base de datos.

MySQL: Base de datos utilizada.



Estructura del Proyecto

Paquetes Principales


Contiene la clase principal donde se maneja la interacción con el usuario y se muestran los menús.

datos

Clases que representan los datos obtenidos desde la API de Gutendex.

modelos

Clases que representan las entidades principales del proyecto como Libro y Autor.

repository

Interfaces que extienden JPA para realizar consultas a la base de datos.

servicios

Clases que encapsulan la lógica de negocio y la interacción con la API.

Clase Principal: Principal

La clase Principal es el punto de entrada de la aplicación y contiene:

Un menú interactivo que permite al usuario realizar las siguientes acciones:

Buscar libros por título directamente desde la web.

Listar libros registrados en la base de datos.

Buscar libros por nombre de autor.

Listar autores por año de actividad.

Filtrar libros por idioma.

Mostrar el top 10 de libros más descargados.

Buscar información sobre autores registrados.

Buscar libros por título en la base de datos.

Funcionalidades

Buscar Libro por Título (Web)

Permite buscar un libro por título directamente desde la API de Gutendex. Si el libro no existe en la base de datos, se guarda automáticamente.

Listar Libros por Título (Base de Datos)

Ordena y muestra los libros almacenados en la base de datos.

Listar Autores por Año

Filtra autores que estuvieron activos en un año específico.

Filtrar Libros por Idioma

Permite buscar libros según su idioma (Español, Inglés, Francés o Portugués).

Top 10 Libros Más Descargados

Muestra los 10 libros más descargados según los datos obtenidos de la API.

Futuras Mejoras

Implementar pruebas unitarias con JUnit.

Crear una interfaz gráfica para mejorar la experiencia de usuario.

Mejorar la validación de datos y manejo de errores.



Contacto: Mellena Mongush

Si tienes alguna pregunta o sugerencia, no dudes en contactarme:

Correo: mellenamongush@gmail.com


