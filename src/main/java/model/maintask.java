package model;

import java.util.HashSet; 
import java.util.Set;
import java.time.LocalDate;

/**
 * Clase principal que ejecuta las operaciones básicas relacionadas con la gestión de tareas.
 * Este es el punto de entrada del programa, donde se inicializan y se muestran detalles de los proyectos,
 * usuarios y tareas.
 */
public class maintask {
    public static void main(String[] args) {
    	// Creación de un nuevo proyecto con identificador, nombre y descripción.
        Project project = new Project(1, "Gestión de Tareas", "Proyecto para gestionar tareas");
        // Creación de un usuario con identificador, nombre de usuario y contraseña.
        User user = new User(1L, "usuario1", "password"); // Usar 1L para Long
        // Creación de un usuario sin identificador explícito.
        User userWithoutId = new User("testuser", "password123");
        // Obtención de los identificadores del proyecto y del usuario.
        Long projectId = Long.valueOf(project.getId()); 
        Long userId = user.getId();
        // Salida de los identificadores del usuario y del proyecto.
        System.out.println("ID del usuario: " + userId);
        System.out.println("ID del proyecto: " + projectId);
        // Creación de una tarea con identificador, descripción, fecha de vencimiento, estado y proyecto asociado.
        Task task = new Task(1L, "Realizar informe mensual", LocalDate.parse("2030-05-30"), "Pendiente", projectId);
        // Salida de los detalles de la tarea
        System.out.println("Detalles de la tarea:");
        System.out.println("ID: " + task.getId());
        System.out.println("Descripción: " + task.getDescription());
        System.out.println("Fecha de vencimiento: " + task.getDueDate());
        System.out.println("Estado: " + task.getStatus());
        System.out.println("ID del usuario: " + user.getId());
    }
}