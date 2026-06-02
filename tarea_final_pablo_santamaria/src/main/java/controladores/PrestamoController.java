package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Prestamo;
import utilidades.JPAUtil;

import java.util.List;

public class PrestamoController extends AbstractCrudController<Prestamo, Integer> {

    public PrestamoController() {
        super(Prestamo.class);
    }

    public void insertarPrestamo(Prestamo prestamo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(prestamo);
            // Al crear un préstamo, marcamos el libro como "prestado" automáticamente
            if (prestamo.getLibro() != null) {
                prestamo.getLibro().setEstado("prestado");
                em.merge(prestamo.getLibro());
            }
            tx.commit();
            System.out.println("Préstamo insertado");
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
            // Si se ha puesto fecha de fin, significa que el libro ha sido devuelto
            // Actualizamos el estado del libro a "disponible"
            if (prestamo.getLibro() != null && prestamo.getFechaFin() != null) {
                prestamo.getLibro().setEstado("disponible");
                em.merge(prestamo.getLibro());
            }
            tx.commit();
            System.out.println("Préstamo actualizado");
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
                // Al eliminar el préstamo, dejamos el libro disponible de nuevo
                if (prestamo.getLibro() != null) {
                    prestamo.getLibro().setEstado("disponible");
                    em.merge(prestamo.getLibro());
                }
                em.remove(prestamo);
                System.out.println("Préstamo eliminado");
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
        return listar();
    }

    public Prestamo buscarPorId(int idPrestamo) {
        return super.buscarPorId(idPrestamo);
    }
}
