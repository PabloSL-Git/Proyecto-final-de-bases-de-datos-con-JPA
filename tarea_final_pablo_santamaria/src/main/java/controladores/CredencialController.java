package controladores;

import modelos.entidades.Credencial;

import java.util.List;

public class CredencialController extends AbstractCrudController<Credencial, Integer> {

    public CredencialController() {
        super(Credencial.class);
    }

    public void insertarCredencial(Credencial credencial) {
        insertar(credencial);
        System.out.println("Credencial insertada");
    }

    public void actualizarCredencial(Credencial credencial) {
        actualizar(credencial);
        System.out.println("Credencial actualizada");
    }

    public void borrarCredencial(int idCredencial) {
        var em = utilidades.JPAUtil.getEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            Credencial credencial = em.find(Credencial.class, idCredencial);
            if (credencial != null) {
                em.remove(credencial);
                System.out.println("Credencial eliminada");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Credencial> listarCredenciales() {
        return listar();
    }

    public Credencial buscarPorId(int idCredencial) {
        return super.buscarPorId(idCredencial);
    }
}
