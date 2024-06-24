package model;

import java.sql.SQLException; 
import java.util.Set;

/**
 * Controlador que gestiona la lógica de negocio relacionada con los roles.
 */
public class RoleController {
    private RoleDAO roleDAO;

    /**
     * Constructor del controlador de roles.
     *
     * @param roleDAO El objeto DAO para acceder a los datos de los roles.
     */
    public RoleController(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    /**
     * Añade un nuevo rol a la base de datos.
     *
     * @param name El nombre del nuevo rol.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void addRole(String name) throws SQLException {
        Role role = new Role(name);
        roleDAO.addRole(role);
    }

    /**
     * Obtiene todos los roles de la base de datos.
     *
     * @return Un conjunto de todos los roles.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public Set<Role> getRoles() throws SQLException {
        return roleDAO.getRoles();
    }
}