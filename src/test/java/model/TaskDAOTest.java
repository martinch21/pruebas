package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskDAOTest {

    private TaskDAO taskDAO;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        taskDAO = new TaskDAO();
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/martinbd", "root", "melmmlam1234*");
        try (Statement statement = connection.createStatement()) {
            // Limpiar la tabla de tareas antes de cada prueba
            statement.execute("DELETE FROM tareas");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Limpiar la tabla de tareas después de cada prueba
            statement.execute("DELETE FROM tareas");
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testAddTask() throws SQLException {
        Task task = new Task(null,"Tarea de prueba", LocalDate.now(), "Pendiente", 1L);
        taskDAO.addTask(task);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM tareas WHERE descripcion = 'Tarea de prueba'")) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt("count"));
        }
    }

    @Test
    void testGetAllTasks() throws SQLException {
        Task task1 = new Task(null,"Tarea de prueba 1", LocalDate.now(), "Pendiente", 1L);
        Task task2 = new Task(null,"Tarea de prueba 2", LocalDate.now().plusDays(1), "Completa", 1L);
        taskDAO.addTask(task1);
        taskDAO.addTask(task2);

        List<Task> tasks = taskDAO.getAllTasks();
        assertEquals(2, tasks.size());
    }

    @Test
    void testUpdateTask() throws SQLException {
        Task task = new Task(null,"Tarea de prueba", LocalDate.now(), "Pendiente", 1L);
        taskDAO.addTask(task);

        task.setDescription("Tarea actualizada");
        task.setStatus("Completa");
        taskDAO.updateTask(task);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT descripcion, estado FROM tareas WHERE id = " + task.getId())) {
            assertTrue(resultSet.next());
            assertEquals("Tarea actualizada", resultSet.getString("descripcion"));
            assertEquals("Completa", resultSet.getString("estado"));
        }
    }

    @Test
    void testDeleteTask() throws SQLException {
        Task task = new Task(null,"Tarea de prueba", LocalDate.now(), "Pendiente", 1L);
        taskDAO.addTask(task);
        taskDAO.deleteTask(task.getId());

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM tareas WHERE id = " + task.getId())) {
            assertTrue(resultSet.next());
            assertEquals(0, resultSet.getInt("count"));
        }
    }
}