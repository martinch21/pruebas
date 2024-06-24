package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO userDAO;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        userDAO = new UserDAO();
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/martinbd", "root", "melmmlam1234*");
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Limpiar la tabla de usuarios después de cada prueba
            statement.execute("DELETE FROM usuarios");
        }
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testAddUser() throws SQLException {
        User user = new User("testuser", "password123");
        userDAO.addUser(user);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM usuarios WHERE username = 'testuser'")) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt("count"));
        }
    }

    @Test
    void testAuthenticate() throws SQLException {
        User user = new User("testuser", "password123");
        userDAO.addUser(user);

        assertTrue(userDAO.authenticate("testuser", "password123"));
        assertFalse(userDAO.authenticate("testuser", "wrongpassword"));
    }
}