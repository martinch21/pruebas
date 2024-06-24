package model;

/**
 * Clase que representa un proyecto en la aplicación.
 * Cada proyecto tiene un identificador único, un nombre y una descripción.
 */
public class Project {
    private int id;
    private String name;
    private String description;

    /**
     * Constructor para crear un nuevo proyecto con los detalles especificados.
     *
     * @param id          el identificador único del proyecto
     * @param name        el nombre del proyecto
     * @param description una breve descripción del proyecto
     */
    public Project(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Obtiene el identificador único del proyecto.
     *
     * @return el identificador del proyecto
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del proyecto.
     *
     * @return el nombre del proyecto
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la descripción del proyecto.
     *
     * @return la descripción del proyecto
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece el nombre del proyecto.
     *
     * @param name el nuevo nombre del proyecto
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Establece la descripción del proyecto.
     *
     * @param description la nueva descripción del proyecto
     */
    public void setDescription(String description) {
        this.description = description;
    }
}