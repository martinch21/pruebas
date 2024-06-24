package model;

import javax.persistence.*;  
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un rol dentro del sistema.
 * Cada rol tiene un identificador único, un nombre y puede estar asociado a varios usuarios.
 */
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * Constructor por defecto.
     */
    public Role() {
    }

    /**
     * Constructor para crear un nuevo rol con el nombre especificado.
     *
     * @param name El nombre del rol.
     */
    public Role(String name) {
        this.name = name;
        this.id = id;
    }
 
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * Obtiene el identificador único del rol.
     *
     * @return el identificador del rol.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del rol.
     *
     * @param id el nuevo identificador del rol.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del rol.
     *
     * @return el nombre del rol.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del rol.
     *
     * @param name el nuevo nombre del rol.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el conjunto de usuarios asociados a este rol.
     *
     * @return el conjunto de usuarios.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Establece el conjunto de usuarios asociados a este rol.
     *
     * @param users el nuevo conjunto de usuarios.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}