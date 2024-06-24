package model;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase que actualiza las contraseñas de los usuarios en la base de datos.
 * Las contraseñas se recuperan en texto plano y se convierten a hashes seguros utilizando BCrypt.
 */
public class UpdateUserPasswords {
    /**
     * Método principal que ejecuta la actualización de las contraseñas de los usuarios.
     * Conecta a la base de datos, recupera las contraseñas en texto plano, las hashea y las actualiza en la base de datos.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/martinbd";
        String usuario = "root";
        String contraseña = "melmmlam1234*";

        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña);
                PreparedStatement statement = connection.prepareStatement("SELECT id, password FROM usuarios");
                ResultSet resultSet = statement.executeQuery()) {
        	
        	while (resultSet.next()) {
                 Long userId = resultSet.getLong("id");
                 String plainPassword = resultSet.getString("password");

                // Hash de la contraseña en texto plano
                String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

                // Actualización del registro del usuario con la contraseña hasheada
                try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE usuarios SET password = ? WHERE id = ?")) {
                    updateStatement.setString(1, hashedPassword);
                    updateStatement.setLong(2, userId);
                    updateStatement.executeUpdate();
                }
            }

            System.out.println("Contraseñas actualizadas correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}