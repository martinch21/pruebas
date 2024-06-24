package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase que verifica los hashes de contraseñas almacenados en la base de datos.
 * Conecta a la base de datos, recupera los hashes de contraseñas y los verifica con contraseñas en texto claro.
 */
public class VerifyPasswordHashes {

    /**
     * Método principal que ejecuta la verificación de los hashes de contraseñas.
     * Conecta a la base de datos, recupera los hashes de contraseñas y los compara con contraseñas en texto claro.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/martinbd";
        String usuario = "root";
        String contraseña = "melmmlam1234*";

        // Conexión a la base de datos y recuperación de hashes de contraseñas
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT username, password FROM usuarios")) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String passwordHash = resultSet.getString("password");

                System.out.println("Usuario: " + username + " - Hash de contraseña: " + passwordHash);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Contraseñas en texto claro
        String plainPasswordAdmin = "password";
        String plainPasswordTestuser = "password123";

        // Hashes almacenados en la base de datos
        String hashAdmin = "$2a$10$/lIIACWEnK2LDXQ735g/Eul09NmCx92.JiqmP89UHJBoNMiSh8leS";
        String hashTestuser = "$2a$10$h8.QSzhn7Y4iODJsaE8Nh.um0JfgQbG2Aul2NCqmL0e5Gth7hU5LK";

        try {
            // Verificación de contraseñas en texto claro con los hashes almacenados
            boolean adminMatches = BCrypt.checkpw(plainPasswordAdmin, hashAdmin);
            boolean testuserMatches = BCrypt.checkpw(plainPasswordTestuser, hashTestuser);

            // Resultados de la verificación
            System.out.println("Admin contraseña correcta: " + adminMatches);
            System.out.println("Testuser contraseña correcta: " + testuserMatches);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al verificar las contraseñas: " + e.getMessage());
        }
    }
}