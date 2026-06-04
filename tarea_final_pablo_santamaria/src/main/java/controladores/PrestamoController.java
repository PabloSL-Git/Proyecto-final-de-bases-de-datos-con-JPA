package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Prestamo;
import utilidades.JPAUtil;


public class PrestamoController extends AbstractCrudController<Prestamo, Integer> {

    public PrestamoController() {
        super(Prestamo.class);
    }

    public void insertarPrestamo(Prestamo prestamo) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(prestamo);
            // Al crear un préstamo, marcamos el libro como "prestado" automáticamente
            if (prestamo.getLibro() != null) {
                prestamo.getLibro().setEstado("prestado");
                entityManager.merge(prestamo.getLibro());
            }
            transaction.commit();
            System.out.println("Préstamo insertado");
        } catch (Exception excepcion) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            excepcion.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void actualizarPrestamo(Prestamo prestamo) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(prestamo);
            // Si se ha puesto fecha de fin, significa que el libro ha sido devuelto
            // Actualizamos el estado del libro a "disponible"
            if (prestamo.getLibro() != null && prestamo.getFechaFin() != null) {
                prestamo.getLibro().setEstado("disponible");
                entityManager.merge(prestamo.getLibro());
            }
            transaction.commit();
            System.out.println("Préstamo actualizado");
        } catch (Exception excepcion) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            excepcion.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void borrarPrestamo(int idPrestamo) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Prestamo prestamo = entityManager.find(Prestamo.class, idPrestamo);
            if (prestamo != null) {
                // Al eliminar el préstamo, dejamos el libro disponible de nuevo
                if (prestamo.getLibro() != null) {
                    prestamo.getLibro().setEstado("disponible");
                    entityManager.merge(prestamo.getLibro());
                }
                entityManager.remove(prestamo);
                System.out.println("Préstamo eliminado");
            }
            transaction.commit();
        } catch (Exception excepcion) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            excepcion.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

}
