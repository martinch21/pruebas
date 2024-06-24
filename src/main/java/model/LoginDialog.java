package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase LoginDialog que proporciona un cuadro de diálogo de inicio de sesión para la autenticación de usuarios.
 * Extiende {@link JDialog} y permite a los usuarios ingresar su nombre de usuario y contraseña para acceder a la aplicación.
 */
public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean authenticated = false;
    private String username;
    private UserDAO userDAO;

    /**
     * Constructor para crear un nuevo cuadro de diálogo de inicio de sesión.
     * 
     * @param parent El componente padre del cuadro de diálogo.
     */
    public LoginDialog(JFrame parent) {
        super(parent, "Inicio de Sesión", true);
        userDAO = new UserDAO();
        initUI();
    }

    /**
     * Inicializa la interfaz de usuario del cuadro de diálogo.
     */
    private void initUI() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                    authenticated = true;
                    username = usernameField.getText();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Usuario o contraseña incorrectos",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getParent());
    }

    /**
     * Autentica al usuario verificando las credenciales ingresadas.
     */
    private boolean authenticate(String username, String password) {
        try (Connection connection = ConexionJDBC.obtenerConexion()) {
            String query = "SELECT password FROM usuarios WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedHash = resultSet.getString("password");
                        return BCrypt.checkpw(password, storedHash);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    /**
     * Verifica si el usuario ha sido autenticado exitosamente.
     * 
     * @return true si el usuario está autenticado, false en caso contrario.
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Obtiene el nombre de usuario del usuario autenticado.
     * 
     * @return el nombre de usuario del usuario autenticado.
     */
    public String getUsername() {
        return username;
    }
}