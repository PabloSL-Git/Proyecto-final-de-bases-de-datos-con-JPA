package controladores;

import modelos.entidades.Autor;

public class AutorController extends AbstractCrudController<Autor, Integer> {

    public AutorController() {
        super(Autor.class);
    }

    @Override
    public void insertar(Autor autor) {
        autor.setIdAutor(siguienteId("idAutor"));
        super.insertar(autor);
    }

    public void borrarAutor(int idAutor) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Autor autor = entityManager.find(Autor.class, idAutor);
            if (autor == null) {
                throw new IllegalStateException("No se encontró el autor con id: " + idAutor);
            }
            long libros = (long) entityManager.createQuery(
                    "SELECT COUNT(l) FROM Libro l WHERE l.autor.idAutor = :id")
                    .setParameter("id", idAutor)
                    .getSingleResult();
            if (libros > 0) {
                throw new IllegalStateException(
                        "No se puede eliminar: el autor tiene " + libros + " libro(s) asociado(s).");
            }
            entityManager.remove(autor);
            System.out.println("Autor eliminado correctamente.");
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
