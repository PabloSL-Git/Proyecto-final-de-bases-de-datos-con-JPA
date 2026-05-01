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
    private BibliotecaController bibliotecaController = new BibliotecaController();
    private LectorController lectorController = new LectorController();
    private CredencialController credencialController = new CredencialController();
    private PrestamoController prestamoController = new PrestamoController();

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
            exportarLector(carpeta);
            exportarPrestamo(carpeta);
            exportarCredencial(carpeta);
            exportarBiblioteca(carpeta);

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
                            l.getEstado());
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
                            a.getNacionalidad());
        }

        pw.close();
    }

    private void exportarBiblioteca(String carpeta) throws Exception {

        List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();

        PrintWriter pw = new PrintWriter(
                new FileWriter(carpeta + "/bibliotecas.csv"));

        pw.println("id,nombre,direccion");

        for (Biblioteca b : bibliotecas) {
            pw.println(
                    b.getIdBiblioteca() + "," +
                            b.getNombre() + "," +
                            b.getDireccion());
        }

        pw.close();
    }

    private void exportarLector(String carpeta) throws Exception {

        List<Lector> lectores = lectorController.listarLectores();

        PrintWriter pw = new PrintWriter(
                new FileWriter(carpeta + "/lectores.csv"));

        pw.println("id,nombre,apellido1,apellido2,email,telefono,id_biblioteca");

        for (Lector l : lectores) {
            int idBiblioteca = (l.getBiblioteca() != null) ? l.getBiblioteca().getIdBiblioteca() : 0;
            pw.println(
                    l.getIdLector() + "," +
                            l.getNombre() + "," +
                            l.getApellido1() + "," +
                            l.getApellido2() + "," +
                            l.getEmail() + "," +
                            l.getTelefono() + "," +
                            idBiblioteca);
        }

        pw.close();
    }

    private void exportarCredencial(String carpeta) throws Exception {

        List<Credencial> credenciales = credencialController.listarCredenciales();

        PrintWriter pw = new PrintWriter(
                new FileWriter(carpeta + "/credenciales.csv"));

        pw.println("id,numero_tarjeta,fecha_emision,id_lector");

        for (Credencial c : credenciales) {
            int idLector = (c.getLector() != null) ? c.getLector().getIdLector() : 0;
            pw.println(
                    c.getIdCredencial() + "," +
                            c.getNumeroTarjeta() + "," +
                            c.getFechaEmision() + "," +
                            idLector);
        }

        pw.close();
    }

    private void exportarPrestamo(String carpeta) throws Exception {

        List<Prestamo> prestamos = prestamoController.listarPrestamos();

        PrintWriter pw = new PrintWriter(
                new FileWriter(carpeta + "/prestamos.csv"));

        pw.println("id,fecha_inicio,fecha_fin,id_lector,id_libro");

        for (Prestamo p : prestamos) {
            int idLector = (p.getLector() != null) ? p.getLector().getIdLector() : 0;
            int idLibro = (p.getLibro() != null) ? p.getLibro().getIdLibro() : 0;
            pw.println(
                    p.getIdPrestamo() + "," +
                            p.getFechaInicio() + "," +
                            p.getFechaFin() + "," +
                            idLector + "," +
                            idLibro);
        }

        pw.close();
    }
}