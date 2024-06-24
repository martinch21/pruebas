package model;

import java.sql.*; 
import java.util.Set;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Clase que gestiona toda la autenticación del usuario.
 * Proporciona métodos para autenticar usuarios y añadir nuevos usuarios a la base de datos.
 */
public class UserDAO {
    private static final String URL = "jdbc:mysql://localhost:3307/martinbd";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "melmmlam1234*";
   
    /**
     * Autentica a un usuario verificando su nombre de usuario y contraseña.
     * 
     * @param username El nombre de usuario.
     * @param password La contraseña en texto plano.
     * @return true si las credenciales son correctas, false en caso contrario.
     */
    public boolean authenticate(String username, String password) {
        String query = "SELECT password FROM usuarios WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    return BCrypt.checkpw(password, storedPassword);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }


    /**
     * Añade un nuevo usuario a la base de datos.
     * 
     * @param user El objeto User que contiene los detalles del usuario.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            statement.setString(1, user.getUsername());
            statement.setString(2, hashedPassword);
            statement.executeUpdate();
        }
    }
    
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String password = resultSet.getString("password");
                    return new User(id, username, password);
                }
            }
        }
        return null;
    }
    
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE usuarios SET username = ?, password = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
        }
    }
    
    public void deleteUser(Long id) throws SQLException {
        String query = "DELETE FROM usuarios WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
    public boolean checkPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }
    public Set<String> getRoles(String username) throws SQLException {
        Set<String> roles = new HashSet<>();
        String query = "SELECT r.name FROM roles r " +
                       "JOIN user_roles ur ON r.id = ur.role_id " +
                       "JOIN usuarios u ON ur.user_id = u.id " +
                       "WHERE u.username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    roles.add(resultSet.getString("name"));
                }
            }
        }
        return roles;
    }
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM usuarios";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                users.add(new User(id, username, password));
            }
        }
        return users;
    }


	
}