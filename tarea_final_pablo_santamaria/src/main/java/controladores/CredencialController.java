package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Credencial;
import utilidades.JPAUtil;

import java.util.List;

public class CredencialController {

    public void insertarCredencial(Credencial credencial) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(credencial);
            tx.commit();
            System.out.println("✔ Credencial insertada");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarCredencial(Credencial credencial) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(credencial);
            tx.commit();
            System.out.println("✔ Credencial actualizada");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void borrarCredencial(int idCredencial) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Credencial credencial = em.find(Credencial.class, idCredencial);
            if (credencial != null) {
                em.remove(credencial);
                System.out.println("✔ Credencial eliminada");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Credencial> listarCredenciales() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Credencial c", Credencial.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Credencial buscarPorId(int idCredencial) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Credencial.class, idCredencial);
        } finally {
            em.close();
        }
    }
}
