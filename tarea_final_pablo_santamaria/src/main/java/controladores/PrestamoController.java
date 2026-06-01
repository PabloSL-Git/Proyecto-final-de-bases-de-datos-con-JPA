package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Prestamo;
import utilidades.JPAUtil;

import java.util.List;

public class PrestamoController {

    public void insertarPrestamo(Prestamo prestamo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(prestamo);
            if (prestamo.getLibro() != null) {
                prestamo.getLibro().setEstado("prestado");
                em.merge(prestamo.getLibro());
            }
            tx.commit();
            System.out.println("✔ Préstamo insertado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarPrestamo(Prestamo prestamo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(prestamo);
            if (prestamo.getLibro() != null && prestamo.getFechaFin() != null) {
                prestamo.getLibro().setEstado("disponible");
                em.merge(prestamo.getLibro());
            }
            tx.commit();
            System.out.println("✔ Préstamo actualizado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void borrarPrestamo(int idPrestamo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Prestamo prestamo = em.find(Prestamo.class, idPrestamo);
            if (prestamo != null) {
                if (prestamo.getLibro() != null) {
                    prestamo.getLibro().setEstado("disponible");
                    em.merge(prestamo.getLibro());
                }
                em.remove(prestamo);
                System.out.println("✔ Préstamo eliminado");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Prestamo> listarPrestamos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Prestamo p", Prestamo.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Prestamo buscarPorId(int idPrestamo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Prestamo.class, idPrestamo);
        } finally {
            em.close();
        }
    }
}
