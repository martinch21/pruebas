package model;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase Main que sirve como el punto de entrada de la aplicación.
 * Gestiona la conexión inicial a la base de datos y lanza la interfaz de usuario principal.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Connection conexion = ConexionJDBC.obtenerConexion();
            System.out.println("Conexión exitosa a la base de datos");
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos:");
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);

            if (loginDialog.isAuthenticated()) {
                TaskDAO taskDAO = new TaskDAO();
                UserDAO userDAO = new UserDAO();
                String currentUser = loginDialog.getUsername();
                TaskManagerUI taskManagerUI = new TaskManagerUI(taskDAO, currentUser);
                new TaskController(taskManagerUI, taskDAO, userDAO, currentUser);
                taskManagerUI.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}