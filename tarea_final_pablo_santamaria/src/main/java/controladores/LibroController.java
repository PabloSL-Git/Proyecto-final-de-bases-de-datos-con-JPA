package controladores;

import modelos.entidades.Libro;

import java.util.List;

public class LibroController extends AbstractCrudController<Libro, Integer> {

    public LibroController() {
        super(Libro.class);
    }

    public void insertarLibro(Libro libro) {
        insertar(libro);
        System.out.println("Libro insertado");
    }

    public void actualizarLibro(Libro libro) {
        actualizar(libro);
        System.out.println("Libro actualizado");
    }

    public void borrarLibro(int idLibro) {
        var em = utilidades.JPAUtil.getEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            Libro libro = em.find(Libro.class, idLibro);
            if (libro != null) {
                long prestamosActivos = (long) em.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.libro.idLibro = :id AND p.fechaFin IS NULL")
                        .setParameter("id", idLibro)
                        .getSingleResult();
                if (prestamosActivos > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el libro tiene "
                                    + prestamosActivos + " préstamo(s) activo(s) sin devolver.");
                }
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
        return listar();
    }

    public Libro buscarPorId(int idLibro) {
        return super.buscarPorId(idLibro);
    }
}
