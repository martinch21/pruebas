package model;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase para generar un hash seguro a partir de una contraseña en texto plano.
 * Utiliza la biblioteca BCrypt para realizar el hash.
 */
public class GeneratePasswordHash {
    /**
     * Método principal que genera y muestra el hash de una contraseña en texto plano.
     * 
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Contraseñas en texto plano que serán hasheadas
        String plainPasswordAdmin = "password";
        String plainPasswordTestuser = "password123";

        // Generar el hash de las contraseñas
        String hashedPasswordAdmin = BCrypt.hashpw(plainPasswordAdmin, BCrypt.gensalt());
        String hashedPasswordTestuser = BCrypt.hashpw(plainPasswordTestuser, BCrypt.gensalt());

        // Imprimir los hashes generados
        System.out.println("Hash para 'password' (admin): " + hashedPasswordAdmin);
        System.out.println("Hash para 'password123' (testuser): " + hashedPasswordTestuser);
    }
}