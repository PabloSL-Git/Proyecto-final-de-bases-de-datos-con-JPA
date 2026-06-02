package controladores;

import modelos.entidades.Lector;

import java.util.List;

public class LectorController extends AbstractCrudController<Lector, Integer> {

    public LectorController() {
        super(Lector.class);
    }

    public void insertarLector(Lector lector) {
        insertar(lector);
        System.out.println("Lector insertado");
    }

    public void actualizarLector(Lector lector) {
        actualizar(lector);
        System.out.println("Lector actualizado");
    }

    public void borrarLector(int idLector) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Lector lector = entityManager.find(Lector.class, idLector);
            if (lector != null) {
                long prestamos = (long) entityManager.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.lector.idLector = :id")
                        .setParameter("id", idLector)
                        .getSingleResult();
                if (prestamos > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el lector tiene " + prestamos + " préstamo(s) asociado(s).");
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

    public List<Lector> listarLectores() {
        return listar();
    }

    public Lector buscarPorId(int idLector) {
        return super.buscarPorId(idLector);
    }
}
