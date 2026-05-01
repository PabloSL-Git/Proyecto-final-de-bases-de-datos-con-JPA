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

            System.out.println("✔ Backup creado en carpeta: " + carpeta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportarBibliotecas(String carpeta) throws Exception {
        List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();
        PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/bibliotecas.csv"));
        pw.println("id,nombre,direccion");
        for (Biblioteca b : bibliotecas) {
            pw.println(b.getIdBiblioteca() + "," + csv(b.getNombre()) + "," + csv(b.getDireccion()));
        }
        pw.close();
    }

    private void exportarAutores(String carpeta) throws Exception {
        List<Autor> autores = autorController.listarAutores();
        PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/autores.csv"));
        pw.println("id,nombre,apellido1,apellido2,nacionalidad");
        for (Autor a : autores) {
            pw.println(a.getIdAutor() + "," + csv(a.getNombre()) + "," +
                    csv(a.getApellido1()) + "," + csv(a.getApellido2()) + "," +
                    csv(a.getNacionalidad()));
        }
        pw.close();
    }

    private void exportarLibros(String carpeta) throws Exception {
        List<Libro> libros = libroController.listarLibros();
        PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/libros.csv"));
        pw.println("id,titulo,anio,estado,id_autor,id_biblioteca");
        for (Libro l : libros) {
            int idAutor = (l.getAutor() != null) ? l.getAutor().getIdAutor() : 0;
            int idBiblioteca = (l.getBiblioteca() != null) ? l.getBiblioteca().getIdBiblioteca() : 0;
            pw.println(l.getIdLibro() + "," + csv(l.getTitulo()) + "," +
                    l.getAnioPublicacion() + "," + csv(l.getEstado()) + "," +
                    idAutor + "," + idBiblioteca);
        }
        pw.close();
    }

    private void exportarLectores(String carpeta) throws Exception {
        List<Lector> lectores = lectorController.listarLectores();
        PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/lectores.csv"));
        pw.println("id,nombre,apellido1,apellido2,email,telefono,id_biblioteca");
        for (Lector l : lectores) {
            int idBiblioteca = (l.getBiblioteca() != null) ? l.getBiblioteca().getIdBiblioteca() : 0;
            pw.println(l.getIdLector() + "," + csv(l.getNombre()) + "," +
                    csv(l.getApellido1()) + "," + csv(l.getApellido2()) + "," +
                    csv(l.getEmail()) + "," + csv(l.getTelefono()) + "," + idBiblioteca);
        }
        pw.close();
    }

    private void exportarCredenciales(String carpeta) throws Exception {
        List<Credencial> credenciales = credencialController.listarCredenciales();
        PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/credenciales.csv"));
        pw.println("id,numero_tarjeta,fecha_emision,id_lector");
        for (Credencial c : credenciales) {
            int idLector = (c.getLector() != null) ? c.getLector().getIdLector() : 0;
            pw.println(c.getIdCredencial() + "," + csv(c.getNumeroTarjeta()) + "," +
                    csv(c.getFechaEmision()) + "," + idLector);
        }
        pw.close();
    }

    private void exportarPrestamos(String carpeta) throws Exception {
        List<Prestamo> prestamos = prestamoController.listarPrestamos();
        PrintWriter pw = new PrintWriter(new FileWriter(carpeta + "/prestamos.csv"));
        pw.println("id,fecha_inicio,fecha_fin,id_lector,id_libro");
        for (Prestamo p : prestamos) {
            int idLector = (p.getLector() != null) ? p.getLector().getIdLector() : 0;
            int idLibro = (p.getLibro() != null) ? p.getLibro().getIdLibro() : 0;
            pw.println(p.getIdPrestamo() + "," + csv(p.getFechaInicio()) + "," +
                    csv(p.getFechaFin()) + "," + idLector + "," + idLibro);
        }
        pw.close();
    }
}
