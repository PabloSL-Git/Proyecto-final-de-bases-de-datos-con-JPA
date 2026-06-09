package controladores;

import modelos.entidades.Credencial;
import modelos.entidades.Lector;

import java.util.List;

public class CredencialController extends AbstractCrudController<Credencial, Integer> {

    public CredencialController() {
        super(Credencial.class);
    }

    @Override
    public void insertar(Credencial credencial) {
        if (credencial.getLector() != null) {
            var em = utilidades.JPAUtil.getEntityManager();
            try {
                long count = (long) em.createQuery(
                        "SELECT COUNT(c) FROM Credencial c WHERE c.lector.idLector = :id")
                        .setParameter("id", credencial.getLector().getIdLector())
                        .getSingleResult();
                if (count > 0) {
                    throw new IllegalStateException(
                            "Este lector ya tiene una credencial asignada.");
                }
            } finally {
                em.close();
            }
        }
        int id = siguienteId("idCredencial");
        credencial.setIdCredencial(id);
        credencial.setNumeroTarjeta(String.format("TARJ-%04d", id));
        super.insertar(credencial);
    }

    public List<Lector> listarLectoresSinCredencial() {
        var em = utilidades.JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT l FROM Lector l WHERE l.credencial IS NULL", Lector.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void borrarCredencial(int idCredencial) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Credencial credencial = entityManager.find(Credencial.class, idCredencial);

            if (credencial == null) {
                throw new IllegalStateException("No se encontró la credencial con id: " + idCredencial);
            }
            if (credencial.getLector() != null) {
                long prestamos = (long) entityManager.createQuery(
                        "SELECT COUNT(p) FROM Prestamo p WHERE p.lector.idLector = :id AND p.fechaFin IS NULL")
                        .setParameter("id", credencial.getLector().getIdLector())
                        .getSingleResult();
                if (prestamos > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el lector de esta credencial tiene "
                                    + prestamos + " préstamo(s) activo(s).");
                }
                credencial.getLector().setCredencial(null);
                entityManager.merge(credencial.getLector());
            }
            entityManager.remove(credencial);
            System.out.println("Credencial eliminada correctamente.");
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
