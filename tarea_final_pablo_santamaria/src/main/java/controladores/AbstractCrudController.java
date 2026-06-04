package controladores;

import jakarta.persistence.EntityManager;
import utilidades.JPAUtil;

import java.util.List;

public abstract class AbstractCrudController<T, ID> {

    private final Class<T> entityClass;

    protected AbstractCrudController(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void insertar(T entidad) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(entidad);
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

    public void actualizar(T entidad) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(entidad);
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

    public List<T> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public T buscarPorId(ID id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    protected int siguienteId(String campo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Number max = (Number) em.createQuery(
                    "SELECT MAX(e." + campo + ") FROM " + entityClass.getSimpleName() + " e")
                    .getSingleResult();
            return max == null ? 1 : max.intValue() + 1;
        } finally {
            em.close();
        }
    }
}
