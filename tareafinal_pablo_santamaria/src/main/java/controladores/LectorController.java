package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Lector;
import utilidades.JPAUtil;

import java.util.List;

public class LectorController {

    // INSERTAR
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

    // ACTUALIZAR
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

    // BORRAR
    public void borrarLector(int idLector) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Lector lector = em.find(Lector.class, idLector);
            if (lector != null) {
                em.remove(lector);
                System.out.println("✔ Lector eliminado");
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
    public List<Lector> listarLectores() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery("SELECT l FROM Lector l", Lector.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // BUSCAR POR ID
    public Lector buscarPorId(int idLector) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Lector.class, idLector);
        } finally {
            em.close();
        }
    }
}
