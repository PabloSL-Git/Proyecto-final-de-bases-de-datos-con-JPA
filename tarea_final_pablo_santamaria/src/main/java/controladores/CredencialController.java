package controladores;

import modelos.entidades.Credencial;

public class CredencialController extends AbstractCrudController<Credencial, Integer> {

    public CredencialController() {
        super(Credencial.class);
    }

    public void borrarCredencial(int idCredencial) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Credencial credencial = entityManager.find(Credencial.class, idCredencial);
            if (credencial != null) {
                entityManager.remove(credencial);
                System.out.println("Credencial eliminada");
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
