package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Libro;
import utilidades.JPAUtil;

import java.util.List;

public class LibroController {

    // INSERTAR LIBRO
    public void insertarLibro(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(libro);
            tx.commit();
            System.out.println("✔ Libro insertado correctamente");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ACTUALIZAR LIBRO
    public void actualizarLibro(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(libro);
            tx.commit();
            System.out.println("✔ Libro actualizado correctamente");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // BORRAR LIBRO
    public void borrarLibro(int idLibro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Libro libro = em.find(Libro.class, idLibro);
            if (libro != null) {
                em.remove(libro);
                System.out.println("✔ Libro eliminado correctamente");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // LISTAR LIBROS
    public List<Libro> listarLibros() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery("SELECT l FROM Libro l", Libro.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // BUSCAR POR ID
    public Libro buscarPorId(int idLibro) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Libro.class, idLibro);
        } finally {
            em.close();
        }
    }
}
