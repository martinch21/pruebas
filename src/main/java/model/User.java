package model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un usuario en el sistema.
 * Cada usuario tiene un identificador único, un nombre de usuario, una contraseña y uno o más roles.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructor para crear un nuevo usuario con el identificador, nombre de usuario y contraseña especificados.
     *
     * @param id       El identificador del usuario.
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     */
    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor para crear un nuevo usuario sin especificar el identificador.
     * El identificador se generará automáticamente.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return el identificador del usuario.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id el nuevo identificador del usuario.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return el nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username el nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return la contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password la nueva contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el conjunto de roles asociados a este usuario.
     *
     * @return el conjunto de roles.
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Establece el conjunto de roles asociados a este usuario.
     *
     * @param roles el nuevo conjunto de roles.
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Obtiene una representación en cadena de texto de los roles del usuario.
     *
     * @return una cadena de texto con los nombres de los roles, separados por comas, o null si no hay roles.
     */
    public String getRole() {
        if (roles.isEmpty()) {
            return null;
        }
        StringBuilder roleNames = new StringBuilder();
        for (Role role : roles) {
            if (roleNames.length() > 0) {
                roleNames.append(", ");
            }
            roleNames.append(role.getName());
        }
        return roleNames.toString();
    //prueba/
    }
}