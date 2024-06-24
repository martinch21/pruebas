package model;

import java.time.LocalDate;

/**
 * Clase que representa una tarea en el sistema de gestión de tareas.
 * Cada tarea tiene un identificador único, una descripción, una fecha de vencimiento, un estado y puede estar asociada a un proyecto específico.
 */
public class Task {
    private Long id;          // Identificador único de la tarea
    private String description;  // Descripción de la tarea
    private LocalDate dueDate;   // Fecha de vencimiento de la tarea
    private String status;       // Estado actual de la tarea (ej. "Completo", "Pendiente")
    private Long projectId;      // Identificador del proyecto al que pertenece la tarea, si aplica

    /**
     * Constructor para crear una nueva tarea con todos los detalles necesarios.
     * @param id El identificador único de la tarea.
     * @param description La descripción de la tarea.
     * @param dueDate La fecha de vencimiento de la tarea.
     * @param status El estado actual de la tarea.
     * @param projectId El identificador del proyecto asociado a la tarea.
     */
    public Task(Long id, String description, LocalDate dueDate, String status, Long projectId) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.projectId = projectId;
    }

    // Métodos getter y setter para cada propiedad.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}