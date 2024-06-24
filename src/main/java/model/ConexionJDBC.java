package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que proporciona la funcionalidad para obtener una conexión JDBC a la base de datos MySQL.
 * Utiliza el controlador de MySQL para establecer la conexión.
 */

public class ConexionJDBC {
    static final String URL = "jdbc:mysql://localhost:3307/martinbd";
    static final String USUARIO = "root";
    static final String CONTRASEÑA = "melmmlam1234*";
    /**
     * Obtiene una conexión a la base de datos MySQL.
     * 
     * @return una conexión a la base de datos MySQL
     * @throws SQLException si ocurre un error al establecer la conexión
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
    }
}