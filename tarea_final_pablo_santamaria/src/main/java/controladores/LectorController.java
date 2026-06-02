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
        var em = utilidades.JPAUtil.getEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            Lector lector = em.find(Lector.class, idLector);
            if (lector != null) {
                long prestamos = (long) em.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.lector.idLector = :id")
                        .setParameter("id", idLector)
                        .getSingleResult();
                if (prestamos > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el lector tiene " + prestamos + " préstamo(s) asociado(s).");
                }
                em.remove(lector);
                System.out.println("Lector eliminado");
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

    public List<Lector> listarLectores() {
        return listar();
    }

    public Lector buscarPorId(int idLector) {
        return super.buscarPorId(idLector);
    }
}
