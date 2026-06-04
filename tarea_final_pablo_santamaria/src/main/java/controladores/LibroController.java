package controladores;

import modelos.entidades.Libro;

public class LibroController extends AbstractCrudController<Libro, Integer> {

    public LibroController() {
        super(Libro.class);
    }

    @Override
    public void insertar(Libro libro) {
        libro.setIdLibro(siguienteId("idLibro"));
        super.insertar(libro);
    }

    public void borrarLibro(int idLibro) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Libro libro = entityManager.find(Libro.class, idLibro);
            if (libro != null) {
                long prestamosActivos = (long) entityManager.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.libro.idLibro = :id AND p.fechaFin IS NULL")
                        .setParameter("id", idLibro)
                        .getSingleResult();
                if (prestamosActivos > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el libro tiene "
                                    + prestamosActivos + " préstamo(s) activo(s) sin devolver.");
                }
                entityManager.createQuery("DELETE FROM Prestamo p WHERE p.libro.idLibro = :id")
                        .setParameter("id", idLibro)
                        .executeUpdate();
                entityManager.remove(libro);
                System.out.println("Libro eliminado");
            }
            transaction.commit();
        } catch (IllegalStateException excepcionEstado) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw excepcionEstado;
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
