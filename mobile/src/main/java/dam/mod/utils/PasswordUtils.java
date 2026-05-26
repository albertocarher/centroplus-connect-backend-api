package dam.mod.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // 1. Generar hash
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // 2. Verificar contraseña
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}