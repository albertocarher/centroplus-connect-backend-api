package dam.mod.utils;
 
import dam.mod.models.Usuario;
import dam.mod.services.IUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import static org.junit.jupiter.api.Assertions.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de Session")
class SessionTest {
 
    @Mock
    private IUsuarioService usuarioService;
 
    @BeforeEach
    void setUp() {
        Session.logout();
        Session.setUsuarioService(null);
    }
 
    @Test
    @DisplayName("getCurrentUser devuelve null cuando no hay sesión")
    void getCurrentUser_sinSesion_null() {
        assertNull(Session.getCurrentUser());
    }
 
    @Test
    @DisplayName("setCurrentUser guarda el usuario y getCurrentUser lo devuelve")
    void setCurrentUser_guardaYRecupera() {
        Usuario user = new Usuario();
        user.setId(1);
        Session.setCurrentUser(user);
        assertEquals(user, Session.getCurrentUser());
    }
 
    @Test
    @DisplayName("setCurrentUser con null no lanza excepción")
    void setCurrentUser_null_noLanzaExcepcion() {
        assertDoesNotThrow(() -> Session.setCurrentUser(null));
    }
 
    @Test
    @DisplayName("setCurrentUser con null deja currentUser como null")
    void setCurrentUser_null_currentUserNull() {
        Usuario user = new Usuario();
        Session.setCurrentUser(user);
        Session.setCurrentUser(null);
        assertNull(Session.getCurrentUser());
    }
 
    @Test
    @DisplayName("logout limpia el usuario de la sesión")
    void logout_limpiaUsuario() {
        Session.setCurrentUser(new Usuario());
        Session.logout();
        assertNull(Session.getCurrentUser());
    }
 
    @Test
    @DisplayName("getUsuarioService devuelve null si no se ha asignado")
    void getUsuarioService_sinAsignar_null() {
        assertNull(Session.getUsuarioService());
    }
 
    @Test
    @DisplayName("setUsuarioService guarda el servicio y getUsuarioService lo devuelve")
    void setUsuarioService_guardaYRecupera() {
        Session.setUsuarioService(usuarioService);
        assertEquals(usuarioService, Session.getUsuarioService());
    }
 
    @Test
    @DisplayName("setUsuarioService con null no lanza excepción")
    void setUsuarioService_null_noLanzaExcepcion() {
        assertDoesNotThrow(() -> Session.setUsuarioService(null));
    }
 
    @Test
    @DisplayName("getTokenSesionGuardado devuelve null si no hay token")
    void getTokenSesionGuardado_sinToken_null() {
        Session.setTokenSesion(null);
        assertNull(Session.getTokenSesionGuardado());
    }
 
    @Test
    @DisplayName("setTokenSesion guarda el token y getTokenSesionGuardado lo devuelve")
    void setTokenSesion_guardaYRecupera() {
        Session.setTokenSesion("mi-token");
        assertEquals("mi-token", Session.getTokenSesionGuardado());
    }
 
    @Test
    @DisplayName("setTokenSesion con null elimina el token guardado")
    void setTokenSesion_null_eliminaToken() {
        Session.setTokenSesion("token-previo");
        Session.setTokenSesion(null);
        assertNull(Session.getTokenSesionGuardado());
    }
 
    @Test
    @DisplayName("logout elimina también el token guardado")
    void logout_eliminaToken() {
        Session.setTokenSesion("token-activo");
        Session.logout();
        assertNull(Session.getTokenSesionGuardado());
    }
}