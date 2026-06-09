package DAO;

import modelos.entidades.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO implements IAutorDAO {

    @Override
    public List<Autor> obtenerTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Autor";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener autores", e);
        }
        return lista;
    }

    @Override
    public Autor buscarPorId(int id) {
        String sql = "SELECT * FROM Autor WHERE id_autor = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar autor", e);
        }
        return null;
    }

    @Override
    public void insertar(Autor autor) {
        String sql = "INSERT INTO Autor (id_autor, nombre, apellido1, apellido2, nacionalidad) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, autor.getIdAutor());
            ps.setString(2, autor.getNombre());
            ps.setString(3, autor.getApellido1());
            ps.setString(4, autor.getApellido2());
            ps.setString(5, autor.getNacionalidad());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar autor", e);
        }
    }

    @Override
    public void borrar(int id) {
        String sql = "DELETE FROM Autor WHERE id_autor = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) return;
            throw new RuntimeException("No se encontró ningún autor con id: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al borrar autor", e);
        }
    }

    @Override
    public void modificar(Autor autor) {
        String sql = "UPDATE Autor SET nombre=?, apellido1=?, apellido2=?, nacionalidad=? WHERE id_autor=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, autor.getNombre());
            ps.setString(2, autor.getApellido1());
            ps.setString(3, autor.getApellido2());
            ps.setString(4, autor.getNacionalidad());
            ps.setInt(5, autor.getIdAutor());
            int filas = ps.executeUpdate();
            if (filas > 0) return;
            throw new RuntimeException("No se encontró ningún autor con id: " + autor.getIdAutor());
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar autor", e);
        }
    }

    // Convierte una fila del ResultSet en un objeto Autor
    private Autor mapear(ResultSet rs) throws SQLException {
        return new Autor(
            rs.getInt("id_autor"),
            rs.getString("nombre"),
            rs.getString("apellido1"),
            rs.getString("apellido2"),
            rs.getString("nacionalidad")
        );
    }
}
