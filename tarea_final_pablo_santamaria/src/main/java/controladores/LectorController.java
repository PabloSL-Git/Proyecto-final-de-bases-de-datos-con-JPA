package controladores;

import modelos.entidades.Lector;

public class LectorController extends AbstractCrudController<Lector, Integer> {

    public LectorController() {
        super(Lector.class);
    }

    @Override
    public void insertar(Lector lector) {
        lector.setIdLector(siguienteId("idLector"));
        super.insertar(lector);
    }

    public void borrarLector(int idLector) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Lector lector = entityManager.find(Lector.class, idLector);

            if (lector == null) {
                throw new IllegalStateException("No se encontró el lector con id: " + idLector);
            }
            long prestamosActivos = (long) entityManager.createQuery(
                    "SELECT COUNT(p) FROM Prestamo p WHERE p.lector.idLector = :id AND p.fechaFin IS NULL")
                    .setParameter("id", idLector)
                    .getSingleResult();
            if (prestamosActivos > 0) {
                throw new IllegalStateException(
                        "No se puede eliminar: el lector tiene " + prestamosActivos + " préstamo(s) activo(s).");
            }
            entityManager.createQuery(
                    "UPDATE Prestamo p SET p.lector = null WHERE p.lector.idLector = :id")
                    .setParameter("id", idLector)
                    .executeUpdate();
            entityManager.remove(lector);
            System.out.println("Lector eliminado correctamente.");
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
            throw new RuntimeException(excepcion);
        } finally {
            entityManager.close();
        }
    }

}
