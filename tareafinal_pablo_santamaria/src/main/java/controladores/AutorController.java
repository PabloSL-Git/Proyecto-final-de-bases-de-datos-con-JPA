package controladores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.Autor;
import utilidades.JPAUtil;

import java.util.List;

public class AutorController {

    // INSERTAR
    public void insertarAutor(Autor autor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(autor);
            tx.commit();
            System.out.println("✔ Autor insertado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ACTUALIZAR
    public void actualizarAutor(Autor autor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(autor);
            tx.commit();
            System.out.println("✔ Autor actualizado");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // BORRAR
    public void borrarAutor(int idAutor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Autor autor = em.find(Autor.class, idAutor);
            if (autor != null) {
                em.remove(autor);
                System.out.println("✔ Autor eliminado");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // LISTAR
    public List<Autor> listarAutores() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery("SELECT a FROM Autor a", Autor.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // BUSCAR POR ID
    public Autor buscarPorId(int idAutor) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Autor.class, idAutor);
        } finally {
            em.close();
        }
    }
}
