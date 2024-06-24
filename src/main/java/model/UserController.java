package model;

import java.sql.SQLException;
import java.util.Set;
import java.util.List;

/**
 * Controlador que gestiona la lógica de negocio relacionada con los usuarios.
 */
public class UserController {
    private UserDAO userDAO;
    private RoleDAO roleDAO;

    /**
     * Constructor del controlador de usuarios.
     *
     * @param userDAO El objeto DAO para acceder a los datos de los usuarios.
     * @param roleDAO El objeto DAO para acceder a los datos de los roles.
     */
    public UserController(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    /**
     * Añade un nuevo usuario al sistema.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void addUser(String username, String password) throws SQLException {
        User user = new User(username, password);
        userDAO.addUser(user);
    }

    /**
     * Asigna un rol a un usuario.
     *
     * @param username El nombre de usuario.
     * @param roleName El nombre del rol.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void assignRoleToUser(String username, String roleName) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        Role role = roleDAO.getRoleByName(roleName);
        if (user != null && role != null) {
            roleDAO.assignRoleToUser(user.getId(), role.getId());
        }
    }

    /**
     * Obtiene los roles de un usuario.
     *
     * @param username El nombre de usuario.
     * @return Un conjunto de nombres de roles.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public Set<String> getUserRoles(String username) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            return roleDAO.getUserRoles(user.getId());
        }
        return null;
    }

    /**
     * Elimina un usuario del sistema.
     *
     * @param username El nombre de usuario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void deleteUser(String username) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            userDAO.deleteUser(user.getId());
        }
    }

    /**
     * Autentica a un usuario verificando su nombre de usuario y contraseña.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @return true si las credenciales son correctas, false en caso contrario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public boolean authenticateUser(String username, String password) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            return userDAO.checkPassword(user, password);
        }
        return false;
    }

    /**
     * Actualiza la información de un usuario.
     *
     * @param username El nombre de usuario.
     * @param newUsername El nuevo nombre de usuario.
     * @param newPassword La nueva contraseña.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void updateUser(String username, String newUsername, String newPassword) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            userDAO.updateUser(user);
        }
    }

    /**
     * Obtiene una lista de todos los usuarios.
     *
     * @return Una lista de todos los usuarios.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    // Otros métodos relevantes para la gestión de usuarios...
}