package dam.mod.repositories;
 
import dam.mod.models.RememberToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de RememberTokenRepositoryImpl")
class RememberTokenRepositoryTest {
 
    @Mock
    private IRememberTokenRepository repository;
 
    private RememberToken tokenValido;
 
    @BeforeEach
    void setUp() {
        tokenValido = new RememberToken(1, 42, "hash_abc123", "2099-12-31 00:00:00");
    }
 
    @Test
    @DisplayName("saveToken devuelve true cuando el insert tiene éxito")
    void saveToken_exito() {
        when(repository.saveToken(42, "hash_abc123", "2099-12-31 00:00:00"))
                .thenReturn(true);
 
        boolean result = repository.saveToken(42, "hash_abc123", "2099-12-31 00:00:00");
 
        assertTrue(result);
        verify(repository).saveToken(42, "hash_abc123", "2099-12-31 00:00:00");
    }
 
    @Test
    @DisplayName("saveToken devuelve false cuando el insert falla")
    void saveToken_fallo() {
        when(repository.saveToken(anyInt(), anyString(), anyString()))
                .thenReturn(false);
 
        boolean result = repository.saveToken(99, "hash_xyz", "2099-01-01 00:00:00");
 
        assertFalse(result);
    }
 
    @Test
    @DisplayName("saveToken lanza RuntimeException ante error de BD")
    void saveToken_excepcion() {
        when(repository.saveToken(anyInt(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Error guardando token"));
 
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> repository.saveToken(1, "hash", "2099-01-01 00:00:00"));
 
        assertEquals("Error guardando token", ex.getMessage());
    }
 
    @Test
    @DisplayName("findByHash devuelve el token cuando existe y no ha expirado")
    void findByHash_encontrado() {
        when(repository.findByHash("hash_abc123")).thenReturn(tokenValido);
 
        RememberToken result = repository.findByHash("hash_abc123");
 
        assertNotNull(result);
        assertEquals(42, result.getUserId());
        assertEquals("hash_abc123", result.getTokenHash());
    }
 
    @Test
    @DisplayName("findByHash devuelve null cuando el hash no existe")
    void findByHash_noEncontrado() {
        when(repository.findByHash("hash_inexistente")).thenReturn(null);
 
        RememberToken result = repository.findByHash("hash_inexistente");
 
        assertNull(result);
    }
 
    @Test
    @DisplayName("findByHash devuelve null cuando el token ha expirado")
    void findByHash_tokenExpirado() {
        when(repository.findByHash("hash_expirado")).thenReturn(null);
 
        RememberToken result = repository.findByHash("hash_expirado");
 
        assertNull(result);
    }
 
    @Test
    @DisplayName("findByHash lanza RuntimeException ante error de BD")
    void findByHash_excepcion() {
        when(repository.findByHash(anyString()))
                .thenThrow(new RuntimeException("Error buscando token"));
 
        assertThrows(RuntimeException.class, () -> repository.findByHash("cualquier_hash"));
    }
 
    @Test
    @DisplayName("findAllValid devuelve lista con tokens vigentes")
    void findAllValid_conTokens() {
        RememberToken token2 = new RememberToken(2, 10, "hash_def456", "2099-06-01 00:00:00");
        when(repository.findAllValid()).thenReturn(Arrays.asList(tokenValido, token2));
 
        List<RememberToken> result = repository.findAllValid();
 
        assertEquals(2, result.size());
        assertEquals("hash_abc123", result.get(0).getTokenHash());
        assertEquals("hash_def456", result.get(1).getTokenHash());
    }
 
    @Test
    @DisplayName("findAllValid devuelve lista vacía cuando no hay tokens vigentes")
    void findAllValid_listaVacia() {
        when(repository.findAllValid()).thenReturn(Collections.emptyList());
 
        List<RememberToken> result = repository.findAllValid();
 
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
 
    @Test
    @DisplayName("findAllValid lanza RuntimeException ante error de BD")
    void findAllValid_excepcion() {
        when(repository.findAllValid())
                .thenThrow(new RuntimeException("Error leyendo tokens"));
 
        assertThrows(RuntimeException.class, () -> repository.findAllValid());
    }
 
    @Test
    @DisplayName("deleteByUserId devuelve true cuando borra al menos un token")
    void deleteByUserId_exito() {
        when(repository.deleteByUserId(42)).thenReturn(true);
 
        boolean result = repository.deleteByUserId(42);
 
        assertTrue(result);
        verify(repository).deleteByUserId(42);
    }
 
    @Test
    @DisplayName("deleteByUserId devuelve false cuando el usuario no tiene tokens")
    void deleteByUserId_sinTokens() {
        when(repository.deleteByUserId(999)).thenReturn(false);
 
        boolean result = repository.deleteByUserId(999);
 
        assertFalse(result);
    }
 
    @Test
    @DisplayName("deleteByUserId lanza RuntimeException ante error de BD")
    void deleteByUserId_excepcion() {
        when(repository.deleteByUserId(anyInt()))
                .thenThrow(new RuntimeException("Error borrando tokens"));
 
        assertThrows(RuntimeException.class, () -> repository.deleteByUserId(1));
    }
}
