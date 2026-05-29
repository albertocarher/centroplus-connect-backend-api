package dam.mod.services;
 
import dam.mod.models.Usuario;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.PasswordUtils;
import dam.mod.utils.Validaciones;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {
 
    @Mock
    private IUsuarioRepository repository;
 
    @InjectMocks
    private UsuarioServiceImpl service;
 
    private Usuario usuario;
 
    private MockedStatic<Validaciones> mockValidaciones;
    private MockedStatic<PasswordUtils> mockPasswordUtils;
 
    @BeforeEach
    void setUp() {
        usuario = new Usuario(1, "Juan García", "12345678A", "juan@example.com", "600000000", "ALUMNO", "$2a$10$hashBcrypt");
 
        mockValidaciones = mockStatic(Validaciones.class);
        mockPasswordUtils = mockStatic(PasswordUtils.class);
    }
 
    @AfterEach
    void tearDown() {
        mockValidaciones.close();
        mockPasswordUtils.close();
    }
 
    @Test
    void findAll_devuelveLista() {
        when(repository.findAll()).thenReturn(List.of(usuario));
 
        List<Usuario> result = service.findAll();
 
        assertEquals(1, result.size());
        verify(repository).findAll();
    }
 
    @Test
    void findAll_listaVacia() {
        when(repository.findAll()).thenReturn(List.of());
 
        assertTrue(service.findAll().isEmpty());
    }
 
    @Test
    void findById_existente_devuelveUsuario() {
        when(repository.findById(1)).thenReturn(usuario);
 
        assertNotNull(service.findById(1));
    }
 
    @Test
    void findById_inexistente_devuelveNull() {
        when(repository.findById(99)).thenReturn(null);
 
        assertNull(service.findById(99));
    }
 
    @Test
    void create_usuarioValido_returnTrue() {
        when(repository.save(usuario)).thenReturn(true);
 
        assertTrue(service.create(usuario));
        verify(repository).save(usuario);
    }
 
    @Test
    void create_usuarioNull_lanzaExcepcion() {
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.create(null)
        );
        assertEquals("Usuario no puede ser null", excepcion.getMessage());
        verifyNoInteractions(repository);
    }
 
    @Test
    void create_repositorioFalla_returnFalse() {
        when(repository.save(usuario)).thenReturn(false);
 
        assertFalse(service.create(usuario));
    }
 
    @Test
    void update_usuarioValido_returnTrue() {
        when(repository.update(usuario)).thenReturn(true);
 
        assertTrue(service.update(usuario));
        verify(repository).update(usuario);
    }
 
    @Test
    void update_usuarioNull_lanzaExcepcion() {
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.update(null)
        );
        assertEquals("Usuario no puede ser null", excepcion.getMessage());
        verifyNoInteractions(repository);
    }
 
    @Test
    void delete_idExistente_returnTrue() {
        when(repository.delete(1)).thenReturn(true);
 
        assertTrue(service.delete(1));
    }
 
    @Test
    void delete_idInexistente_returnFalse() {
        when(repository.delete(99)).thenReturn(false);
 
        assertFalse(service.delete(99));
    }
 
    @Test
    void login_credencialesCorrectas_devuelveUsuario() {
        when(repository.findByDni("12345678A")).thenReturn(usuario);
        mockPasswordUtils.when(() -> PasswordUtils.checkPassword("pass123", usuario.getPassword()))
                         .thenReturn(true);
 
        Usuario result = service.login("12345678A", "pass123");
 
        assertNotNull(result);
        assertEquals("12345678A", result.getDni());
    }
 
    @Test
    void login_dniNull_lanzaExcepcion() {
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.login(null, "pass123")
        );
        assertEquals("DNI obligatorio", excepcion.getMessage());
        verifyNoInteractions(repository);
    }
 
    @Test
    void login_dniVacio_lanzaExcepcion() {
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.login("  ", "pass123")
        );
        assertEquals("DNI obligatorio", excepcion.getMessage());
    }
 
    @Test
    void login_passwordNull_lanzaExcepcion() {
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.login("12345678A", null)
        );
        assertEquals("Password obligatoria", excepcion.getMessage());
        verifyNoInteractions(repository);
    }
 
    @Test
    void login_passwordVacia_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> service.login("12345678A", "")
        );
        assertEquals("Password obligatoria", ex.getMessage());
    }
 
    @Test
    void login_usuarioNoExiste_lanzaExcepcion() {
        when(repository.findByDni("00000000X")).thenReturn(null);
 
        RuntimeException excepcion = assertThrows(
            RuntimeException.class,
            () -> service.login("00000000X", "pass123")
        );
        assertEquals("Usuario no existe", excepcion.getMessage());
    }
 
    @Test
    void login_passwordIncorrecta_lanzaExcepcion() {
        when(repository.findByDni("12345678A")).thenReturn(usuario);
        mockPasswordUtils.when(() -> PasswordUtils.checkPassword("mal", usuario.getPassword()))
                         .thenReturn(false);
 
        RuntimeException excepcion = assertThrows(
            RuntimeException.class,
            () -> service.login("12345678A", "mal")
        );
        assertEquals("Credenciales incorrectas", excepcion.getMessage());
    }
 
    @Test
    void findByDni_dniValido_devuelveUsuario() {
        when(repository.findByDni("12345678A")).thenReturn(usuario);
 
        assertNotNull(service.findByDni("12345678A"));
    }
 
    @Test
    void findByDni_dniNull_lanzaExcepcion() {
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.findByDni(null)
        );
        assertEquals("DNI obligatorio", excepcion.getMessage());
        verifyNoInteractions(repository);
    }
 
    @Test
    void findByDni_dniVacio_lanzaExcepcion() {
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.findByDni("")
        );
        assertEquals("DNI obligatorio", excepcion.getMessage());
    }
 
    @Test
    void findByDni_usuarioNoExiste_devuelveNull() {
        when(repository.findByDni("99999999Z")).thenReturn(null);
 
        assertNull(service.findByDni("99999999Z"));
    }
}

