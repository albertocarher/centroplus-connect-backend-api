package dam.mod.repositories.sqlite;
 
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
 
import static org.junit.jupiter.api.Assertions.*;
 
class ConnectionManagerTest {
 
    @Test
    void getConnection_devuelveConexionValida() throws SQLException {
        Connection conn = ConnectionManager.getConnection();
 
        assertNotNull(conn);
        assertFalse(conn.isClosed());
 
        conn.close();
    }
 
    @Test
    void getConnection_conexionEsSQLite() throws SQLException {
        Connection conn = ConnectionManager.getConnection();
 
        String url = conn.getMetaData().getURL();
        assertTrue(url.contains("sqlite"));
 
        conn.close();
    }
 
    @Test
    void getConnection_cadaLlamadaDevuelveNuevaConexion() throws SQLException {
        Connection conn1 = ConnectionManager.getConnection();
        Connection conn2 = ConnectionManager.getConnection();
 
        assertNotSame(conn1, conn2);
 
        conn1.close();
        conn2.close();
    }
}