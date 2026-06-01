package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Libro;
import utilidades.JPAUtil;

import java.util.List;

public class LibroController {

    public void insertarLibro(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(libro);
            tx.commit();
            System.out.println("Libro insertado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizarLibro(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(libro);
            tx.commit();
            System.out.println("Libro actualizado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void borrarLibro(int idLibro) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Libro libro = em.find(Libro.class, idLibro);
            if (libro != null) {
                // Bloqueamos el borrado si el libro tiene préstamos activos (aún no devueltos)
                // Un préstamo activo tiene fecha_fin = null
                long prestamosActivos = (long) em.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.libro.idLibro = :id AND p.fechaFin IS NULL")
                        .setParameter("id", idLibro)
                        .getSingleResult();
                if (prestamosActivos > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el libro tiene "
                            + prestamosActivos + " préstamo(s) activo(s) sin devolver.");
                }
                // Borramos los préstamos históricos (ya devueltos) para no violar la clave foránea
                em.createQuery("DELETE FROM Prestamo p WHERE p.libro.idLibro = :id")
                        .setParameter("id", idLibro)
                        .executeUpdate();
                em.remove(libro);
                System.out.println("Libro eliminado");
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

    public List<Libro> listarLibros() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Libro buscarPorId(int idLibro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Libro.class, idLibro);
        } finally {
            em.close();
        }
    }
}
