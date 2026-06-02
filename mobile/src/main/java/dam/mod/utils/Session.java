package dam.mod.utils;

import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import java.util.prefs.Preferences;

public class Session {

    /**
     * Guarda el usuario
     */
    private static Usuario currentUser;

    /**
     * Servicio de usuario de la aplicación
     */
    private static IUsuarioService usuarioService;

    /**
     * Guarda datos en el sistema
     */
    private static final Preferences prefs = Preferences.userRoot().node("CentroPlusConnect");

    private static final String claveToken = "token_sesion";

    /**
     * Devuelve el usuario actual
     *
     * @return el usuario
     */
    public static Usuario getCurrentUser() {
        return currentUser;
    }

    /**
     * Guarda la sesión
     *
     * @param usuario el usuario
     */
    public static void setCurrentUser(Usuario usuario) {
        currentUser = usuario;

        if (usuario != null) {
            prefs.putInt("usuario_id", usuario.getId());
        }
    }

    /**
     * Rcuperar ID del usuario guardado
     *
     * @return el id del usuario
     */
    public static int getSavedUserId() {
        return prefs.getInt("usuario_id", -1);
    }

    /**
     * Guarda o elimina el token de sesión almacenado en las preferencias de la
     * aplicación.
     *
     * @param token Token de sesión que se desea almacenar. Si es null, se eliminará
     *              el token guardado.
     */
    public static void setTokenSesion(String token) {

        if (token != null) {
            prefs.put(claveToken, token);
        } else {
            prefs.remove(claveToken);
        }
    }

    /**
     * Recupera el token de sesión almacenado en las preferencias de la aplicación.
     *
     * @return El token de sesión guardado si existe. Devuelve null si no hay ningún
     *         token almacenado.
     */
    public static String getTokenSesionGuardado() {
        return prefs.get(claveToken, null);
    }

    /**
     * Cierra la sesión
     */
    public static void logout() {
        currentUser = null;

        prefs.remove("usuario_id");
        prefs.remove(claveToken);
    }

    /**
     * Guarda el servicio de usuario global de la aplicación
     *
     * @param service servicio de usuario
     */
    public static void setUsuarioService(IUsuarioService service) {
        usuarioService = service;
    }

    /**
     * Devuelve el servicio de usuario global de la aplicación
     *
     * @return servicio de usuario
     */
    public static IUsuarioService getUsuarioService() {
        return usuarioService;
    }
}