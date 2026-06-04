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
            if (lector != null) {
                long prestamos = (long) entityManager.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.lector.idLector = :id AND p.fechaFin IS NULL")
                        .setParameter("id", idLector)
                        .getSingleResult();
                if (prestamos > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el lector tiene " + prestamos + " préstamo(s) activo(s) sin devolver.");
                }
                entityManager.remove(lector);
                System.out.println("Lector eliminado");
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
