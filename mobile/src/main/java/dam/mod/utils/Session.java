package dam.mod.utils;

import dam.mod.models.Usuario;

public class Session {

    private static Usuario currentUser;

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Usuario usuario) {
        currentUser = usuario;
    }

    public static void logout() {
        currentUser = null;
    }
}