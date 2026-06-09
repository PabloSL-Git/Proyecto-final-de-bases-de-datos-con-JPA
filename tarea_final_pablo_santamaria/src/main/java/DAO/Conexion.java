package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/Biblioteca_Pablo";
    private static final String USUARIO = "pablo";
    private static final String CONTRASENA = "1234";

    // Devuelve una nueva conexión cada vez que se llama
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}