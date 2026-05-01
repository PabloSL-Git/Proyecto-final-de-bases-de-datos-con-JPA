package utilidades;

import modelos.entidades.*;
import controladores.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BackupManager {

    private LibroController libroController = new LibroController();
    private AutorController autorController = new AutorController();

    public void crearBackup() {

        try {
            // 1. Crear carpeta con fecha
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

            String carpeta = "backup_" + timestamp;
            java.io.File dir = new java.io.File(carpeta);
            dir.mkdir();

            // 2. Exportar tablas
            exportarLibros(carpeta);
            exportarAutores(carpeta);

            System.out.println("✔ Backup creado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportarLibros(String carpeta) throws Exception {

        List<Libro> libros = libroController.listarLibros();

        PrintWriter pw = new PrintWriter(
                new FileWriter(carpeta + "/libros.csv"));

        pw.println("id,titulo,anio,estado");

        for (Libro l : libros) {
            pw.println(
                    l.getIdLibro() + "," +
                    l.getTitulo() + "," +
                    l.getAnioPublicacion() + "," +
                    l.getEstado()
            );
        }

        pw.close();
    }

    private void exportarAutores(String carpeta) throws Exception {

        List<Autor> autores = autorController.listarAutores();

        PrintWriter pw = new PrintWriter(
                new FileWriter(carpeta + "/autores.csv"));

        pw.println("id,nombre,apellido1,apellido2,nacionalidad");

        for (Autor a : autores) {
            pw.println(
                    a.getIdAutor() + "," +
                    a.getNombre() + "," +
                    a.getApellido1() + "," +
                    a.getApellido2() + "," +
                    a.getNacionalidad()
            );
        }

        pw.close();
    }
}