package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que maneja el acceso a la base de datos para las tareas, proporcionando métodos para agregar, actualizar, eliminar y recuperar tareas.
 */
public class TaskDAO {
    private static final String URL = "jdbc:mysql://localhost:3307/martinbd";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "melmmlam1234*";

    /**
     * Agrega una nueva tarea a la base de datos.
     * 
     * @param task La tarea a agregar.
     * @return true si la tarea se agregó correctamente, false de lo contrario.
     * @throws SQLException Si ocurre un error durante la operación de base de datos.
     */
    public boolean addTask(Task task) throws SQLException {
        String query = "INSERT INTO tareas (descripcion, fecha_vencimiento, estado) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, task.getDescription());
            statement.setDate(2, Date.valueOf(task.getDueDate()));
            statement.setString(3, task.getStatus());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        task.setId(generatedKeys.getLong(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Actualiza una tarea existente en la base de datos.
     * 
     * @param task La tarea con los datos actualizados.
     * @throws SQLException Si ocurre un error durante la operación de base de datos.
     */
    public void updateTask(Task task) throws SQLException {
        String query = "UPDATE tareas SET descripcion = ?, fecha_vencimiento = ?, estado = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, task.getDescription());
            statement.setDate(2, Date.valueOf(task.getDueDate()));
            statement.setString(3, task.getStatus());
            statement.setLong(4, task.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Elimina una tarea de la base de datos basándose en su ID.
     * 
     * @param id El ID de la tarea a eliminar.
     * @throws SQLException Si ocurre un error durante la operación de base de datos.
     */
    public void deleteTask(Long id) throws SQLException {
        String query = "DELETE FROM tareas WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    /**
     * Recupera una tarea por su ID.
     * 
     * @param id El ID de la tarea a recuperar.
     * @return La tarea encontrada o null si no existe.
     * @throws SQLException Si ocurre un error durante la operación de base de datos.
     */
    public Task getTaskById(Long id) throws SQLException {
        String query = "SELECT * FROM tareas WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String description = resultSet.getString("descripcion");
                    LocalDate dueDate = resultSet.getDate("fecha_vencimiento").toLocalDate();
                    String status = resultSet.getString("estado");
                    return new Task(id, description, dueDate, status, null); // projectId is not handled here
                }
            }
        }
        return null;
    }

    /**
     * Recupera todas las tareas de la base de datos.
     * 
     * @return Una lista de todas las tareas.
     * @throws SQLException Si ocurre un error durante la operación de base de datos.
     */
    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tareas";
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("descripcion");
                LocalDate dueDate = resultSet.getDate("fecha_vencimiento").toLocalDate();
                String status = resultSet.getString("estado");
                Task task = new Task(id, description, dueDate, status, null);
                tasks.add(task);
            }
        }
        return tasks;
    }
    //prueba//
}