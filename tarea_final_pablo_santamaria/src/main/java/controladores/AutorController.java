package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Autor;
import utilidades.JPAUtil;

import java.util.List;

public class AutorController {

    public void insertarAutor(Autor autor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(autor);
            tx.commit();
            System.out.println("✔ Autor insertado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarAutor(Autor autor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(autor);
            tx.commit();
            System.out.println("✔ Autor actualizado");
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
            Autor autor = em.find(Autor.class, idAutor);
            if (autor != null) {
                long libros = (long) em.createQuery(
                        "SELECT COUNT(l) FROM Libro l WHERE l.autor.idAutor = :id")
                        .setParameter("id", idAutor).getSingleResult();
                if (libros > 0) throw new IllegalStateException(
                        "No se puede eliminar: el autor tiene " + libros + " libro(s) asociado(s).");
                em.remove(autor);
                System.out.println("✔ Autor eliminado");
            }
            tx.commit();
        } catch (IllegalStateException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
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
            return em.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Autor buscarPorId(int idAutor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Autor.class, idAutor);
        } finally {
            em.close();
        }
    }
}
