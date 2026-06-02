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
        var em = utilidades.JPAUtil.getEntityManager();
        var tx = em.getTransaction();
        try {
            tx.begin();
            Autor autor = em.find(Autor.class, idAutor);
            if (autor != null) {
                long libros = (long) em.createQuery(
                        "SELECT COUNT(l) FROM Libro l WHERE l.autor.idAutor = :id")
                        .setParameter("id", idAutor)
                        .getSingleResult();
                if (libros > 0) {
                    throw new IllegalStateException(
                            "No se puede eliminar: el autor tiene " + libros + " libro(s) asociado(s).");
                }
                em.remove(autor);
                System.out.println("Autor eliminado");
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

    public List<Autor> listarAutores() {
        return listar();
    }

    public Autor buscarPorId(int idAutor) {
        return super.buscarPorId(idAutor);
    }
}
