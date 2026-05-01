package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Biblioteca;
import utilidades.JPAUtil;

import java.util.List;

public class BibliotecaController {

    // INSERTAR
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

    // ACTUALIZAR
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

    // BORRAR
    public void borrarBiblioteca(int idBiblioteca) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Biblioteca biblioteca = em.find(Biblioteca.class, idBiblioteca);
            if (biblioteca != null) {
                em.remove(biblioteca);
                System.out.println("✔ Biblioteca eliminada");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // LISTAR
    public List<Biblioteca> listarBibliotecas() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery("SELECT b FROM Biblioteca b", Biblioteca.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // BUSCAR POR ID
    public Biblioteca buscarPorId(int idBiblioteca) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Biblioteca.class, idBiblioteca);
        } finally {
            em.close();
        }
    }
}
