package utilidades;

import controladores.*;
import modelos.entidades.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BackupManager {

    private LibroController libroController = new LibroController();
    private AutorController autorController = new AutorController();
    private BibliotecaController bibliotecaController = new BibliotecaController();
    private LectorController lectorController = new LectorController();
    private CredencialController credencialController = new CredencialController();
    private PrestamoController prestamoController = new PrestamoController();

    // Escapa campos que puedan contener comas para no romper el CSV
    private String csv(Object valor) {
        if (valor == null) return "";
        String s = valor.toString();
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }

    public void crearBackup() {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

            String carpeta = "backup_" + timestamp;
            java.io.File dir = new java.io.File(carpeta);
            dir.mkdir();

            exportarBibliotecas(carpeta);
            exportarAutores(carpeta);
            exportarLibros(carpeta);
            exportarLectores(carpeta);
            exportarCredenciales(carpeta);
            exportarPrestamos(carpeta);

            System.out.println("Backup creado en carpeta: " + carpeta);

        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    private void exportarBibliotecas(String carpeta) throws Exception {
        List<Biblioteca> bibliotecas = bibliotecaController.listar();
        try (PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/bibliotecas.csv"))) {
            pw.println("id,nombre,direccion");
            for (Biblioteca biblioteca : bibliotecas) {
                pw.println(biblioteca.getIdBiblioteca() + "," + csv(biblioteca.getNombre()) + "," + csv(biblioteca.getDireccion()));
            }
        }
    }

    private void exportarAutores(String carpeta) throws Exception {
        List<Autor> autores = autorController.listar();
        try (PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/autores.csv"))) {
            pw.println("id,nombre,apellido1,apellido2,nacionalidad");
            for (Autor autor : autores) {
                pw.println(autor.getIdAutor() + "," + csv(autor.getNombre()) + "," +
                        csv(autor.getApellido1()) + "," + csv(autor.getApellido2()) + "," +
                        csv(autor.getNacionalidad()));
            }
        }
    }

    private void exportarLibros(String carpeta) throws Exception {
        List<Libro> libros = libroController.listar();
        try (PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/libros.csv"))) {
            pw.println("id,titulo,anio,estado,id_autor,id_biblioteca");
            for (Libro libro : libros) {
                int idAutor;
                if (libro.getAutor() != null) {
                    idAutor = libro.getAutor().getIdAutor();
                } else {
                    idAutor = 0;
                }

                int idBiblioteca;
                if (libro.getBiblioteca() != null) {
                    idBiblioteca = libro.getBiblioteca().getIdBiblioteca();
                } else {
                    idBiblioteca = 0;
                }

                pw.println(libro.getIdLibro() + "," + csv(libro.getTitulo()) + "," +
                        libro.getAnioPublicacion() + "," + csv(libro.getEstado()) + "," +
                        idAutor + "," + idBiblioteca);
            }
        }
    }

    private void exportarLectores(String carpeta) throws Exception {
        List<Lector> lectores = lectorController.listar();
        try (PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/lectores.csv"))) {
            pw.println("id,nombre,apellido1,apellido2,email,telefono,id_biblioteca");
            for (Lector lector : lectores) {
                int idBiblioteca;
                if (lector.getBiblioteca() != null) {
                    idBiblioteca = lector.getBiblioteca().getIdBiblioteca();
                } else {
                    idBiblioteca = 0;
                }

                pw.println(lector.getIdLector() + "," + csv(lector.getNombre()) + "," +
                        csv(lector.getApellido1()) + "," + csv(lector.getApellido2()) + "," +
                        csv(lector.getEmail()) + "," + csv(lector.getTelefono()) + "," + idBiblioteca);
            }
        }
    }

    private void exportarCredenciales(String carpeta) throws Exception {
        List<Credencial> credenciales = credencialController.listar();
        try (PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/credenciales.csv"))) {
            pw.println("id,numero_tarjeta,fecha_emision,id_lector");
            for (Credencial credencial : credenciales) {
                int idLector;
                if (credencial.getLector() != null) {
                    idLector = credencial.getLector().getIdLector();
                } else {
                    idLector = 0;
                }

                pw.println(credencial.getIdCredencial() + "," + csv(credencial.getNumeroTarjeta()) + "," +
                        csv(credencial.getFechaEmision()) + "," + idLector);
            }
        }
    }

    private void exportarPrestamos(String carpeta) throws Exception {
        List<Prestamo> prestamos = prestamoController.listar();
        try (PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/prestamos.csv"))) {
            pw.println("id,fecha_inicio,fecha_fin,id_lector,id_libro");
            for (Prestamo prestamo : prestamos) {
                int idLector;
                if (prestamo.getLector() != null) {
                    idLector = prestamo.getLector().getIdLector();
                } else {
                    idLector = 0;
                }

                int idLibro;
                if (prestamo.getLibro() != null) {
                    idLibro = prestamo.getLibro().getIdLibro();
                } else {
                    idLibro = 0;
                }

                pw.println(prestamo.getIdPrestamo() + "," + csv(prestamo.getFechaInicio()) + "," +
                        csv(prestamo.getFechaFin()) + "," + idLector + "," + idLibro);
            }
        }
    }
}
