package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Biblioteca;
import utilidades.JPAUtil;

import java.util.List;

public class BibliotecaController {

    public void insertarBiblioteca(Biblioteca biblioteca) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(biblioteca);
            tx.commit();
            System.out.println("✔ Biblioteca insertada");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarBiblioteca(Biblioteca biblioteca) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(biblioteca);
            tx.commit();
            System.out.println("✔ Biblioteca actualizada");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void borrarBiblioteca(int idBiblioteca) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Biblioteca biblioteca = em.find(Biblioteca.class, idBiblioteca);
            if (biblioteca != null) {
                long libros = (long) em.createQuery(
                        "SELECT COUNT(l) FROM Libro l WHERE l.biblioteca.idBiblioteca = :id")
                        .setParameter("id", idBiblioteca).getSingleResult();
                long lectores = (long) em.createQuery(
                        "SELECT COUNT(l) FROM Lector l WHERE l.biblioteca.idBiblioteca = :id")
                        .setParameter("id", idBiblioteca).getSingleResult();
                if (libros > 0 || lectores > 0) throw new IllegalStateException(
                        "No se puede eliminar: la biblioteca tiene " + libros + " libro(s) y " + lectores + " lector(es) asociado(s).");
                em.remove(biblioteca);
                System.out.println("✔ Biblioteca eliminada");
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

    public List<Biblioteca> listarBibliotecas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT b FROM Biblioteca b", Biblioteca.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Biblioteca buscarPorId(int idBiblioteca) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Biblioteca.class, idBiblioteca);
        } finally {
            em.close();
        }
    }
}
