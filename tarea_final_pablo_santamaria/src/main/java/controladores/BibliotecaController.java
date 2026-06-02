package controladores;

import modelos.entidades.Biblioteca;

import java.util.List;

public class BibliotecaController extends AbstractCrudController<Biblioteca, Integer> {

    public BibliotecaController() {
        super(Biblioteca.class);
    }

    public void insertarBiblioteca(Biblioteca biblioteca) {
        insertar(biblioteca);
        System.out.println("Biblioteca insertada");
    }

    public void actualizarBiblioteca(Biblioteca biblioteca) {
        actualizar(biblioteca);
        System.out.println("Biblioteca actualizada");
    }

    public void borrarBiblioteca(int idBiblioteca) {
        var em = utilidades.JPAUtil.getEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            Biblioteca biblioteca = em.find(Biblioteca.class, idBiblioteca);
            if (biblioteca != null) {
                long libros = (long) em.createQuery(
                        "SELECT COUNT(l) FROM Libro l WHERE l.biblioteca.idBiblioteca = :id")
                        .setParameter("id", idBiblioteca)
                        .getSingleResult();
                long lectores = (long) em.createQuery(
                        "SELECT COUNT(l) FROM Lector l WHERE l.biblioteca.idBiblioteca = :id")
                        .setParameter("id", idBiblioteca)
                        .getSingleResult();
                if (libros > 0 || lectores > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: la biblioteca tiene "
                                    + libros + " libro(s) y " + lectores + " lector(es) asociado(s).");
                }
                em.remove(biblioteca);
                System.out.println("Biblioteca eliminada");
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

    public List<Biblioteca> listarBibliotecas() {
        return listar();
    }

    public Biblioteca buscarPorId(int idBiblioteca) {
        return super.buscarPorId(idBiblioteca);
    }
}
