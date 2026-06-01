package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Lector;
import utilidades.JPAUtil;

import java.util.List;

public class LectorController {

    public void insertarLector(Lector lector) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(lector);
            tx.commit();
            System.out.println("✔ Lector insertado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarLector(Lector lector) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(lector);
            tx.commit();
            System.out.println("✔ Lector actualizado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void borrarLector(int idLector) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Lector lector = em.find(Lector.class, idLector);
            if (lector != null) {
                long prestamos = (long) em.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.lector.idLector = :id")
                        .setParameter("id", idLector).getSingleResult();
                if (prestamos > 0) throw new IllegalStateException(
                        "No se puede eliminar: el lector tiene " + prestamos + " préstamo(s) asociado(s).");
                em.remove(lector);
                System.out.println("✔ Lector eliminado");
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

    public List<Lector> listarLectores() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Lector l", Lector.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Lector buscarPorId(int idLector) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Lector.class, idLector);
        } finally {
            em.close();
        }
    }
}
