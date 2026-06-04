package controladores;

import modelos.entidades.Biblioteca;

public class BibliotecaController extends AbstractCrudController<Biblioteca, Integer> {

    public BibliotecaController() {
        super(Biblioteca.class);
    }

    @Override
    public void insertar(Biblioteca biblioteca) {
        biblioteca.setIdBiblioteca(siguienteId("idBiblioteca"));
        super.insertar(biblioteca);
    }

    public void borrarBiblioteca(int idBiblioteca) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Biblioteca biblioteca = entityManager.find(Biblioteca.class, idBiblioteca);
            if (biblioteca != null) {
                long libros = (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Libro l WHERE l.biblioteca.idBiblioteca = :id")
                        .setParameter("id", idBiblioteca)
                        .getSingleResult();
                long lectores = (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lector l WHERE l.biblioteca.idBiblioteca = :id")
                        .setParameter("id", idBiblioteca)
                        .getSingleResult();
                if (libros > 0 || lectores > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: la biblioteca tiene "
                                    + libros + " libro(s) y " + lectores + " lector(es) asociado(s).");
                }
                entityManager.remove(biblioteca);
                System.out.println("Biblioteca eliminada");
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
