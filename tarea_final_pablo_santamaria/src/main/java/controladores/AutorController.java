package controladores;

import modelos.entidades.Autor;

import java.util.List;

public class AutorController extends AbstractCrudController<Autor, Integer> {

    public AutorController() {
        super(Autor.class);
    }

    public void insertarAutor(Autor autor) {
        insertar(autor);
        System.out.println("Autor insertado");
    }

    public void actualizarAutor(Autor autor) {
        actualizar(autor);
        System.out.println("Autor actualizado");
    }

    public void borrarAutor(int idAutor) {
        var entityManager = utilidades.JPAUtil.getEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Autor autor = entityManager.find(Autor.class, idAutor);
            if (autor != null) {
                long libros = (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Libro l WHERE l.autor.idAutor = :id")
                        .setParameter("id", idAutor)
                        .getSingleResult();
                if (libros > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el autor tiene " + libros + " libro(s) asociado(s).");
                }
                entityManager.remove(autor);
                System.out.println("Autor eliminado");
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

    public List<Autor> listarAutores() {
        return listar();
    }

    public Autor buscarPorId(int idAutor) {
        return super.buscarPorId(idAutor);
    }
}
