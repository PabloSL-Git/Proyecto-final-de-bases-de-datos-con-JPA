package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Autor;
import utilidades.JPAUtil;

import java.util.List;

public class AutorController {

    public void insertarAutor(Autor autor) {
        // Obtenemos un EntityManager (conexión con la BD gestionada por JPA)
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();          // Iniciamos la transacción
            em.persist(autor);   // Indicamos a JPA que inserte este objeto en la BD
            tx.commit();         // Confirmamos y guardamos los cambios
            System.out.println("Autor insertado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback(); // Si falla, deshacemos los cambios
            e.printStackTrace();
        } finally {
            em.close(); // Siempre cerramos el EntityManager al terminar
        }
    }

    public void actualizarAutor(Autor autor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // merge: actualiza un objeto que puede haber sido modificado fuera del contexto JPA
            em.merge(autor);
            tx.commit();
            System.out.println("Autor actualizado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void borrarAutor(int idAutor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Autor autor = em.find(Autor.class, idAutor); // Buscamos el autor por su ID
            if (autor != null) {
                // Comprobamos integridad referencial: si hay libros que usan este autor,
                // MySQL rechazaría el borrado con una excepción de clave foránea.
                // Lo comprobamos antes para dar un mensaje claro al usuario.
                long libros = (long) em.createQuery(
                        "SELECT COUNT(l) FROM Libro l WHERE l.autor.idAutor = :id")
                        .setParameter("id", idAutor)
                        .getSingleResult();
                if (libros > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el autor tiene " + libros + " libro(s) asociado(s).");
                }
                em.remove(autor);
                System.out.println("Autor eliminado");
            }
            tx.commit();
        } catch (IllegalStateException e) {
            if (tx.isActive()) tx.rollback();
            throw e; // Re-lanzamos para que la vista muestre el aviso al usuario
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Autor> listarAutores() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL: lenguaje de consulta de JPA, similar a SQL pero usando nombres de clases Java
            return em.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Autor buscarPorId(int idAutor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Autor.class, idAutor); // Devuelve null si no existe
        } finally {
            em.close();
        }
    }
}
