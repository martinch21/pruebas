package model;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Controlador que gestiona las interacciones entre la vista y el modelo para la funcionalidad relacionada con las tareas.
 * Proporciona métodos para agregar, eliminar y editar tareas, así como cargar tareas desde la base de datos.
 */
public class TaskController {
    private TaskManagerUI view;  // La interfaz de usuario para la gestión de tareas
    private TaskDAO model;       // El acceso a datos de las tareas
    private UserDAO userDAO;     // El acceso a datos de los usuarios
    private String currentUser;  // El usuario actualmente autenticado en la aplicación
    private boolean isInitialized = false;  // Indicador de si el controlador ha sido inicializado

    /**
     * Constructor que inicializa el controlador con la vista, el modelo, el acceso a datos del usuario, y el usuario actual.
     * @param view La interfaz de usuario para la gestión de tareas
     * @param model El modelo de acceso a datos para las tareas
     * @param userDAO El acceso a datos de los usuarios
     * @param currentUser El nombre del usuario actualmente autenticado
     */
    public TaskController(TaskManagerUI view, TaskDAO model, UserDAO userDAO, String currentUser) {
        this.view = view;
        this.model = model;
        this.userDAO = userDAO;
        this.currentUser = currentUser;
        initController();
    }

    /**
     * Inicializa el controlador, configurando los manejadores de eventos para los botones y cargando las tareas iniciales.
     */
    private void initController() {
        if (isInitialized) {
            System.out.println("Controller is already initialized.");
            return;
        }
        System.out.println("Initializing controller...");
        view.getAddButton().addActionListener(e -> addTask());
        view.getDeleteButton().addActionListener(e -> deleteTask());
        view.getEditButton().addActionListener(e -> editTask());
        loadTasks();
        
        isInitialized = true;
    }
    
    /**
     * Carga las tareas desde la base de datos y las muestra en la tabla de la interfaz de usuario.
     */
    private void loadTasks() {
        view.getTableModel().setRowCount(0);  // Limpiar la tabla antes de cargar datos
        try {
            List<Task> tasks = model.getAllTasks();
            for (Task task : tasks) {
                Object[] rowData = {task.getId(), task.getDescription(), task.getDueDate(), task.getStatus()};
                view.getTableModel().addRow(rowData);
            }
        } catch (SQLException e) {
            view.showError("Error al cargar las tareas desde la base de datos.");
        }
    }

    /**
     * Actualiza la tabla de tareas recargando las tareas desde la base de datos.
     */
    private void updateTable() {
        view.getTableModel().setRowCount(0);
        loadTasks();
    }

    /**
     * Agrega una nueva tarea a la base de datos usando la información ingresada en la interfaz de usuario.
     */
    private void addTask() {
        if (!hasPermission("admin")) {
            view.showError("No tienes permisos para realizar esta acción.");
            return;
        }

        String input = view.getInputField().getText().trim();
        System.out.println("Input recibido: " + input); // Mensaje de depuración

        if (input.isEmpty()) {
            view.showError("La entrada no puede estar vacía.");
            return;
        }

        String[] parts = input.split(",");
        System.out.println("Partes del input: " + Arrays.toString(parts)); // Mensaje de depuración

        if (parts.length != 3) {
            view.showError("Formato de entrada incorrecto. Use 'descripcion,fecha_vencimiento,estado'.");
            return;
        }

        String descripcion = parts[0].trim();
        String fechaVencimiento = parts[1].trim();
        String estado = parts[2].trim();

        if (descripcion.isEmpty() || fechaVencimiento.isEmpty() || estado.isEmpty()) {
            view.showError("Todos los campos son obligatorios y no deben estar vacíos.");
            return;
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(fechaVencimiento, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            view.showError("Formato de fecha incorrecto. Utilice YYYY-MM-DD.");
            return;
        }

        try {
            Task task = new Task(null,descripcion, parsedDate, estado, null);
            if (model.addTask(task)) {
                updateTable();
                view.getInputField().setText(""); // Limpiar el campo de entrada después de agregar con éxito
            } else {
                view.showError("Error al agregar la tarea. Por favor revise los datos e intente de nuevo.");
            }
        } catch (SQLException e) {
            view.showError("Error de base de datos al intentar agregar la tarea.");
        }
    }

    /**
     * Elimina la tarea seleccionada en la tabla de la interfaz de usuario.
     */
    private void deleteTask() {
        if (!hasPermission("admin")) {
            view.showError("No tienes permisos para realizar esta acción.");
            return;
        }

        int selectedRow = view.getTaskTable().getSelectedRow();
        if (selectedRow != -1) {
            Long taskId = (Long) view.getTaskTable().getValueAt(selectedRow, 0);
            try {
                model.deleteTask(taskId);
                updateTable();
            } catch (SQLException e) {
                view.showError("Error de base de datos al intentar eliminar la tarea.");
            }
        } else {
            view.showError("Por favor seleccione una tarea para eliminar.");
        }
    }

    /**
     * Edita la tarea seleccionada en la tabla de la interfaz de usuario utilizando la nueva información ingresada por el usuario.
     */
    private void editTask() {
        int selectedRow = view.getTaskTable().getSelectedRow();
        if (selectedRow == -1) {
            view.showError("Por favor seleccione una tarea para editar.");
            return; // Asegura que no proceda si no hay una fila seleccionada.
        }

        Long taskId = (Long) view.getTaskTable().getValueAt(selectedRow, 0);
        if (taskId == null) {
            view.showError("Error al obtener el ID de la tarea.");
            return;
        }

        // Solicita la nueva información de la tarea
        String nuevaDescripcion = JOptionPane.showInputDialog("Ingrese la nueva descripción de la tarea:");
        if (nuevaDescripcion == null || nuevaDescripcion.isEmpty()) return; // Salir si el usuario cancela o ingresa un valor vacío

        String nuevaFechaVencimiento = JOptionPane.showInputDialog("Ingrese la nueva fecha de vencimiento (YYYY-MM-DD):");
        if (nuevaFechaVencimiento == null || nuevaFechaVencimiento.isEmpty()) return; // Salir si el usuario cancela o ingresa un valor vacío

        String nuevoEstado = JOptionPane.showInputDialog("Ingrese el nuevo estado de la tarea:");
        if (nuevoEstado == null || nuevoEstado.isEmpty()) return; // Salir si el usuario cancela o ingresa un valor vacío

        try {
            LocalDate parsedDate = LocalDate.parse(nuevaFechaVencimiento, DateTimeFormatter.ISO_LOCAL_DATE);
            Task task = model.getTaskById(taskId);
            if (task != null) {
                task.setDescription(nuevaDescripcion);
                task.setDueDate(parsedDate);
                task.setStatus(nuevoEstado);
                model.updateTask(task);
                updateTable();
                view.getTaskTable().setRowSelectionInterval(selectedRow, selectedRow); // Re-selecciona la fila editada
            } else {
                view.showError("La tarea no existe.");
            }
        } catch (DateTimeParseException e) {
            view.showError("Formato de fecha incorrecto. Utilice YYYY-MM-DD.");
        } catch (SQLException e) {
            view.showError("Error de base de datos al intentar actualizar la tarea.");
        }
    }

    /**
     * Verifica si el usuario actual tiene el permiso necesario para realizar una acción específica.
     * @param requiredRole El rol requerido para realizar la acción.
     * @return true si el usuario tiene el permiso, false en caso contrario.
     */
    private boolean hasPermission(String requiredRole) {
        try {
            Set<String> roles = userDAO.getRoles(currentUser);
            return roles.contains(requiredRole);
        } catch (SQLException e) {
            e.printStackTrace();
            view.showError("Error al verificar los roles del usuario.");
            return false;
        }
    }
}