package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * Proporciona la interfaz gráfica de usuario para gestionar tareas.
 * Incluye métodos para inicializar componentes de la GUI, cargar datos de tareas y manejar eventos de botones.
 */
public class TaskManagerUI extends JFrame {

    private DefaultTableModel model;
    private JTable taskTable;
    private JButton addButton, deleteButton, editButton;
    private JTextField inputField;
    private String currentUser;
    private UserDAO userDAO;
    private TaskDAO taskDAO;
    private boolean dataLoaded = false;

    /**
     * Constructor que inicializa la interfaz de usuario y establece la conexión con los DAO necesarios.
     *
     * @param taskDAO DAO para operaciones relacionadas con tareas.
     * @param currentUser Nombre de usuario del usuario actual para verificar permisos.
     */
    public TaskManagerUI(TaskDAO taskDAO, String currentUser) {
        this.currentUser = currentUser;
        this.taskDAO = taskDAO;
        userDAO = new UserDAO();
        setTitle("Task Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        checkPermissions();
        initTableData();
    }

    /**
     * Carga los datos de las tareas desde la base de datos y los muestra en la tabla.
     */
    private void initTableData() {
        if (dataLoaded) return;
        dataLoaded = true;
        try {
            List<Task> tasks = taskDAO.getAllTasks();
            model.setRowCount(0);
            for (Task task : tasks) {
                model.addRow(new Object[]{task.getId(), task.getDescription(), task.getDueDate(), task.getStatus()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar las tareas desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Inicializa los componentes de la GUI.
     */
    private void initComponents() {
        JLabel titleLabel = new JLabel("Task Manager");
        inputField = new JTextField(20);
        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");
        editButton = new JButton("Edit Task");

        JPanel topPanel = new JPanel();
        topPanel.add(titleLabel);
        topPanel.add(inputField);
        topPanel.add(addButton);
        topPanel.add(deleteButton);
        topPanel.add(editButton);
        add(topPanel, BorderLayout.NORTH);

        model = new NonEditableTableModel(new Object[]{"ID", "Descripción", "Fecha de vencimiento", "Estado"}, 0);
        taskTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Verifica los permisos del usuario actual para habilitar o deshabilitar funciones.
     */
    private void checkPermissions() {
        try {
            Set<String> roles = userDAO.getRoles(currentUser);
            if (!roles.contains("admin")) {
                addButton.setEnabled(false);
                deleteButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar los roles del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Verifica si una tarea existe en la base de datos.
     *
     * @param taskId ID de la tarea a verificar.
     * @return true si la tarea existe, false de lo contrario.
     */
    private boolean doesTaskExist(Long taskId) {
        try (Connection connection = DriverManager.getConnection(ConexionJDBC.URL, ConexionJDBC.USUARIO, ConexionJDBC.CONTRASEÑA)) {
            String query = "SELECT COUNT(*) FROM tareas WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, taskId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Añade una nueva tarea a la base de datos.
     *
     * @param descripcion Descripción de la nueva tarea.
     * @param fechaVencimiento Fecha de vencimiento de la nueva tarea.
     * @param estado Estado actual de la nueva tarea.
     * @param model Modelo de tabla en el que se añadirá la nueva tarea.
     */
    private void addTask(String descripcion, String fechaVencimiento, String estado, DefaultTableModel model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(fechaVencimiento);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Por favor use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection connection = DriverManager.getConnection(ConexionJDBC.URL, ConexionJDBC.USUARIO, ConexionJDBC.CONTRASEÑA)) {
            Long nextId = 1L;
            String maxIdQuery = "SELECT MAX(id) AS max_id FROM tareas";
            try (PreparedStatement maxIdStatement = connection.prepareStatement(maxIdQuery);
                 ResultSet resultSet = maxIdStatement.executeQuery()) {
                if (resultSet.next()) {
                    nextId = resultSet.getLong("max_id") + 1;
                }
            }

            String query = "INSERT INTO tareas (id, descripcion, fecha_vencimiento, estado) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, nextId);
                statement.setString(2, descripcion);
                statement.setString(3, fechaVencimiento);
                statement.setString(4, estado);
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    model.addRow(new Object[]{nextId, descripcion, fechaVencimiento, estado});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina una tarea de la base de datos.
     *
     * @param taskId ID de la tarea a eliminar.
     * @throws SQLException Si ocurre un error de conexión a la base de datos.
     */
    private void deleteTask(Long taskId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(ConexionJDBC.URL, ConexionJDBC.USUARIO, ConexionJDBC.CONTRASEÑA)) {
            String query = "DELETE FROM tareas WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, taskId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object value = model.getValueAt(i, 0);
                        if (value != null && value instanceof Long) {
                            Long id = (Long) value;
                            if (id.equals(taskId)) {
                                model.removeRow(i);
                                break;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Tarea eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró la tarea con ID: " + taskId, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Edita una tarea existente en la base de datos.
     *
     * @param taskId ID de la tarea a editar.
     * @param nuevaDescripcion Nueva descripción para la tarea.
     * @param nuevaFechaVencimiento Nueva fecha de vencimiento para la tarea.
     * @param nuevoEstado Nuevo estado para la tarea.
     * @param model Modelo de tabla en el que se actualizarán los datos de la tarea.
     * @throws SQLException Si ocurre un error de conexión a la base de datos.
     */
    private void editTask(Long taskId, String nuevaDescripcion, String nuevaFechaVencimiento, String nuevoEstado, DefaultTableModel model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(nuevaFechaVencimiento);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Por favor use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DriverManager.getConnection(ConexionJDBC.URL, ConexionJDBC.USUARIO, ConexionJDBC.CONTRASEÑA)) {
            String query = "UPDATE tareas SET descripcion = ?, fecha_vencimiento = ?, estado = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, nuevaDescripcion);
                statement.setString(2, nuevaFechaVencimiento);
                statement.setString(3, nuevoEstado);
                statement.setLong(4, taskId);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object idValue = model.getValueAt(i, 0);
                        if (idValue != null && idValue instanceof Long) {
                            Long id = (Long) idValue;
                            if (id.equals(taskId)) {
                                model.setValueAt(nuevaDescripcion, i, 1);
                                model.setValueAt(nuevaFechaVencimiento, i, 2);
                                model.setValueAt(nuevoEstado, i, 3);
                                break;
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Tarea actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar la tarea.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getTaskTable() {
        return taskTable;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

class NonEditableTableModel extends DefaultTableModel {
    public NonEditableTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}