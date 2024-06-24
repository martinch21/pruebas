package model;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que gestiona el acceso a los datos de los roles en la base de datos.
 * Proporciona métodos para agregar roles, obtener roles, asignar roles a usuarios y recuperar roles de usuarios.
 */
public class RoleDAO {

    private static final String URL = "jdbc:mysql://localhost:3307/martinbd";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "melmmlam1234*";

    /**
     * Inserta un nuevo rol en la base de datos.
     * 
     * @param role El objeto Role que contiene la información del rol a añadir.
     * @throws SQLException Si hay un error durante la conexión o la ejecución de la consulta.
     */
    public void addRole(Role role) throws SQLException {
        String query = "INSERT INTO roles (name) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, role.getName());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    role.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    /**
     * Recupera un rol por su nombre.
     * 
     * @param name El nombre del rol a buscar.
     * @return El objeto Role si se encuentra, null en caso contrario.
     * @throws SQLException Si ocurre un problema de acceso a la base de datos.
     */
    public Role getRoleByName(String name) throws SQLException {
        String query = "SELECT * FROM roles WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    return new Role(id, name);
                }
            }
        }
        return null;
    }

    /**
     * Obtiene todos los roles disponibles en la base de datos.
     * 
     * @return Un conjunto de objetos Role con todos los roles.
     * @throws SQLException Si hay un error al acceder a la base de datos.
     */
    public Set<Role> getRoles() throws SQLException {
        Set<Role> roles = new HashSet<>();
        String sql = "SELECT * FROM roles";

        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                Role role = new Role(id, name);
                roles.add(role);
            }
        }
        return roles;
    }

    /**
     * Asigna un rol a un usuario en la base de datos.
     * 
     * @param userId El ID del usuario al que se asignará el rol.
     * @param roleId El ID del rol que se asignará al usuario.
     * @throws SQLException Si ocurre un error durante la conexión o la ejecución de la consulta.
     */
    public void assignRoleToUser(Long userId, Long roleId) throws SQLException {
        String query = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.setLong(2, roleId);
            statement.executeUpdate();
        }
    }

    /**
     * Obtiene los roles de un usuario específico.
     * 
     * @param userId El ID del usuario cuyos roles se desean recuperar.
     * @return Un conjunto de strings que representan los nombres de los roles del usuario.
     * @throws SQLException Si hay un error durante la conexión o la ejecución de la consulta.
     */
    public Set<String> getUserRoles(Long userId) throws SQLException {
        Set<String> roles = new HashSet<>();
        String query = "SELECT r.name FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    roles.add(resultSet.getString("name"));
                }
            }
        }
        return roles;
    }
    //prueba//
}