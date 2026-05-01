package utilidades;

import controladores.AutorController;
import controladores.LibroController;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Autor;
import modelos.entidades.Libro;

import java.io.BufferedReader;
import java.io.FileReader;

public class RestoreManager {

    private AutorController autorController = new AutorController();
    private LibroController libroController = new LibroController();

    public void restaurar(String carpeta) {

        try {

            // 1. BORRAR
            borrarTodo();

            // 2. RESTAURAR AUTORES
            restaurarAutores(carpeta + "/autores.csv");

            // 3. RESTAURAR LIBROS
            restaurarLibros(carpeta + "/libros.csv");

            System.out.println("✔ Restauración completada");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------
    // BORRADO GENERAL
    // -------------------------
    private void borrarTodo() {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Orden IMPORTANTE por claves foráneas
            em.createQuery("DELETE FROM Prestamo").executeUpdate();
            em.createQuery("DELETE FROM Credencial").executeUpdate();
            em.createQuery("DELETE FROM Libro").executeUpdate();
            em.createQuery("DELETE FROM Lector").executeUpdate();
            em.createQuery("DELETE FROM Autor").executeUpdate();
            em.createQuery("DELETE FROM Biblioteca").executeUpdate();

            tx.commit();

            System.out.println("✔ Base de datos limpiada correctamente");

        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // -------------------------
    // AUTORES
    // -------------------------
    private void restaurarAutores(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // saltar cabecera

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Autor a = new Autor();
            a.setIdAutor(Integer.parseInt(data[0]));
            a.setNombre(data[1]);
            a.setApellido1(data[2]);
            a.setApellido2(data[3]);
            a.setNacionalidad(data[4]);

            autorController.insertarAutor(a);
        }

        br.close();
    }

    // -------------------------
    // LIBROS
    // -------------------------
    private void restaurarLibros(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // cabecera

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Libro l = new Libro();
            l.setIdLibro(Integer.parseInt(data[0]));
            l.setTitulo(data[1]);
            l.setAnioPublicacion(Integer.parseInt(data[2]));
            l.setEstado(data[3]);

            libroController.insertarLibro(l);
        }

        br.close();
    }
}