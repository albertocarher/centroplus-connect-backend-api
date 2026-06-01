package dam.mod.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @Test
    void hashPasswordGeneraHashDistinto() {
        String password = "MiPassword123";

        String hash = PasswordUtils.hashPassword(password);

        assertNotNull(hash);
        assertNotEquals(password, hash);
    }

    @Test
    void hashPasswordGeneraHashesDiferentesParaMismaPassword() {
        String password = "MiPassword123";

        String hash1 = PasswordUtils.hashPassword(password);
        String hash2 = PasswordUtils.hashPassword(password);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void checkPasswordCorrectaDevuelveTrue() {
        String password = "PasswordSegura";

        String hash = PasswordUtils.hashPassword(password);

        assertTrue(
                PasswordUtils.checkPassword(password, hash)
        );
    }

    @Test
    void checkPasswordIncorrectaDevuelveFalse() {
        String passwordCorrecta = "PasswordSegura";
        String passwordIncorrecta = "OtraPassword";

        String hash = PasswordUtils.hashPassword(passwordCorrecta);

        assertFalse(
                PasswordUtils.checkPassword(passwordIncorrecta, hash)
        );
    }

    @Test
    void checkPasswordHashManipuladoDevuelveFalse() {
        String password = "PasswordSegura";

        String hash = PasswordUtils.hashPassword(password);

        // Manipular hash
        String hashAlterado = hash.substring(0, hash.length() - 1) + "A";

        assertFalse(
                PasswordUtils.checkPassword(password, hashAlterado)
        );
    }

    @Test
    void hashGeneradoEsCompatibleConCheckPassword() {
        String password = "Admin1234";

        String hash = PasswordUtils.hashPassword(password);

        boolean resultado = PasswordUtils.checkPassword(password, hash);

        assertTrue(resultado);
    }
}
