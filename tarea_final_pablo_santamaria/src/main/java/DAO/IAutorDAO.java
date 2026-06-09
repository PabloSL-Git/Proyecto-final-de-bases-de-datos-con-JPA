package DAO;

import modelos.entidades.Autor;
import java.util.List;

public interface IAutorDAO {
    List<Autor> obtenerTodos();
    Autor buscarPorId(int id);
    void insertar(Autor autor);
    void borrar(int id);
    void modificar(Autor autor);
}