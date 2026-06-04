package utilidades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestoreManager {

    private File[] obtenerCopias() {
        File[] files = new File(".").listFiles((d, name) -> name.startsWith("backup_"));
        if (files == null) return new File[0];
        Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
        return files;
    }

    public String obtenerUltimaCopia() {
        File[] files = obtenerCopias();
        return files.length > 0 ? files[0].getAbsolutePath() : null;
    }

    public List<String> listarTodasLasCopias() {
        List<String> copias = new ArrayList<>();
        for (File copia : obtenerCopias()) {
            copias.add(copia.getAbsolutePath());
        }
        return copias;
    }


    // RESTAURAR

    public void restaurar(String carpeta) {
        borrarTodo();

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Orden importante: primero entidades sin dependencias
            restaurarBibliotecas(em, carpeta + "/bibliotecas.csv");
            restaurarAutores(em, carpeta + "/autores.csv");
            restaurarLibros(em, carpeta + "/libros.csv");
            restaurarLectores(em, carpeta + "/lectores.csv");
            restaurarCredenciales(em, carpeta + "/credenciales.csv");
            restaurarPrestamos(em, carpeta + "/prestamos.csv");

            tx.commit();
            System.out.println(" Restauración completada");
        } catch (Exception excepcion) {
            if (tx.isActive()) {
                tx.rollback();
            }
            excepcion.printStackTrace();
        } finally {
            em.close();
        }
    }


    // BORRADO GENERAL

    private void borrarTodo() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM Prestamo").executeUpdate();
            em.createQuery("DELETE FROM Credencial").executeUpdate();
            em.createQuery("DELETE FROM Libro").executeUpdate();
            em.createQuery("DELETE FROM Lector").executeUpdate();
            em.createQuery("DELETE FROM Autor").executeUpdate();
            em.createQuery("DELETE FROM Biblioteca").executeUpdate();
            tx.commit();
            System.out.println(" Base de datos limpiada correctamente");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


    // BIBLIOTECAS

    private void restaurarBibliotecas(EntityManager em, String file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // cabecera
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                Biblioteca biblioteca = new Biblioteca();
                biblioteca.setIdBiblioteca(Integer.parseInt(data[0]));
                biblioteca.setNombre(data[1]);
                if (data.length > 2) {
                    biblioteca.setDireccion(data[2]);
                } else {
                    biblioteca.setDireccion("");
                }
                em.persist(biblioteca);
            }
        }
    }


    // AUTORES

    private void restaurarAutores(EntityManager em, String file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // cabecera
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                Autor autor = new Autor();
                autor.setIdAutor(Integer.parseInt(data[0]));
                autor.setNombre(data[1]);
                autor.setApellido1(data[2]);
                autor.setApellido2(data[3]);
                autor.setNacionalidad(data[4]);
                em.persist(autor);
            }
        }
    }


    // LIBROS (con autor y biblioteca)

    private void restaurarLibros(EntityManager em, String file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // cabecera
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                Libro libro = new Libro();
                libro.setIdLibro(Integer.parseInt(data[0]));
                libro.setTitulo(data[1]);
                libro.setAnioPublicacion(Integer.parseInt(data[2]));
                libro.setEstado(data[3]);

                if (data.length > 4 && !data[4].isEmpty() && !data[4].equals("0")) {
                    libro.setAutor(em.find(Autor.class, Integer.parseInt(data[4])));
                }

                if (data.length > 5 && !data[5].isEmpty() && !data[5].equals("0")) {
                    libro.setBiblioteca(em.find(Biblioteca.class, Integer.parseInt(data[5])));
                }

                em.persist(libro);
            }
        }
    }


    // LECTORES

    private void restaurarLectores(EntityManager em, String file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // cabecera
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                Lector lector = new Lector();
                lector.setIdLector(Integer.parseInt(data[0]));
                lector.setNombre(data[1]);
                lector.setApellido1(data[2]);
                lector.setApellido2(data[3]);
                lector.setEmail(data[4]);
                lector.setTelefono(data[5]);

                if (data.length > 6 && !data[6].isEmpty() && !data[6].equals("0")) {
                    lector.setBiblioteca(em.find(Biblioteca.class, Integer.parseInt(data[6])));
                }

                em.persist(lector);
            }
        }
    }


    // CREDENCIALES

    private void restaurarCredenciales(EntityManager em, String file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // cabecera
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                Credencial credencial = new Credencial();
                credencial.setIdCredencial(Integer.parseInt(data[0]));
                credencial.setNumeroTarjeta(data[1]);

                if (data.length > 2 && !data[2].isEmpty() && !data[2].equals("null")) {
                    credencial.setFechaEmision(LocalDate.parse(data[2]));
                }

                if (data.length > 3 && !data[3].isEmpty() && !data[3].equals("0")) {
                    credencial.setLector(em.find(Lector.class, Integer.parseInt(data[3])));
                }

                em.persist(credencial);
            }
        }
    }


    // PRÉSTAMOS

    private void restaurarPrestamos(EntityManager em, String file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // cabecera
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(Integer.parseInt(data[0]));
                prestamo.setFechaInicio(LocalDate.parse(data[1]));

                if (data.length > 2 && !data[2].isEmpty() && !data[2].equals("null")) {
                    prestamo.setFechaFin(LocalDate.parse(data[2]));
                }

                if (data.length > 3 && !data[3].isEmpty() && !data[3].equals("0")) {
                    prestamo.setLector(em.find(Lector.class, Integer.parseInt(data[3])));
                }

                if (data.length > 4 && !data[4].isEmpty() && !data[4].equals("0")) {
                    prestamo.setLibro(em.find(Libro.class, Integer.parseInt(data[4])));
                }

                em.persist(prestamo);
            }
        }
    }
}

