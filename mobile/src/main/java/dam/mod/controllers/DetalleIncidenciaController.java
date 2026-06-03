package dam.mod.controllers;

import dam.mod.models.Incidencia;
import dam.mod.repositories.IIncidenciaRepository;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.IncidenciaRepository;
import dam.mod.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IIncidenciaService;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.IncidenciaServiceImpl;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controlador del detalle de una incidencia.
 *
 * Permite visualizar la información completa de una incidencia,
 * cambiar su estado a cerrada y volver a la pantalla de listado.
 */
public class DetalleIncidenciaController {

    @FXML
    private Label mensajeLabel;
    /**
     * Asunto de la incidencia.
     */
    @FXML
    private Label lblAsunto;

    /**
     * Estado actual de la incidencia.
     */
    @FXML
    private Label lblEstado;

    /**
     * Fecha de creación de la incidencia.
     */
    @FXML
    private Label lblFecha;

    /**
     * Descripción completa de la incidencia.
     */
    @FXML
    private TextArea txtDescripcion;

    /**
     * Botón para cerrar la incidencia.
     */
    @FXML
    private Button btnCerrar;

    private IIncidenciaService incidenciaService;

    private Incidencia inc;

    /**
     * Inicializa el controlador.
     *
     * Crea los servicios necesarios y carga la incidencia seleccionada.
     */
    @FXML
    public void initialize() {

        IIncidenciaRepository repo = new IncidenciaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepo, new RememberTokenRepositoryImpl());

        incidenciaService = new IncidenciaServiceImpl(repo, usuarioService);

        cargarIncidencia();
    }

    /**
     * Carga la incidencia seleccionada desde el ScreenManager.
     *
     * Si no existe, redirige al listado de incidencias.
     */
    private void cargarIncidencia() {

        int id = ScreenManager.getIncidenciaId();

        inc = incidenciaService.findById(id);

        if (inc == null) {
            mensajeLabel.setText("Incidencia no encontrada");
            ScreenManager.change("incidencias.fxml");
            return;
        }

        lblAsunto.setText(
                inc.getAsunto() != null ? inc.getAsunto() : "");

        lblEstado.setText(
                "Estado: " + (inc.getEstado() != null ? inc.getEstado() : "DESCONOCIDO"));

        lblFecha.setText("Fecha: " + inc.getFecha());

        txtDescripcion.setText(
                inc.getDescripcion() != null ? inc.getDescripcion() : "");

        actualizarBoton();
    }

    /**
     * Actualiza el estado del botón según el estado de la incidencia.
     *
     * Si la incidencia está cerrada, desactiva el botón.
     */
    private void actualizarBoton() {

        if (btnCerrar == null || inc == null)
            return;

        boolean cerrada = inc.getEstado() != null &&
                inc.getEstado().equalsIgnoreCase("CERRADA");

        btnCerrar.setDisable(cerrada);
    }

    /**
     * Cierra la incidencia cambiando su estado a CERRADA.
     */
    @FXML
    private void cerrarIncidencia() {

        if (inc == null)
            return;

        boolean ok = incidenciaService.cambiarEstado(inc.getId(), "CERRADA");

        if (ok) {
            mensajeLabel.setText("Incidencia cerrada");
            inc = incidenciaService.findById(inc.getId());
            cargarIncidencia();
        } else {
            mensajeLabel.setText("Error al cerrar incidencia");
        }
    }

    /**
     * Vuelve a la pantalla de listado de incidencias.
     */
    @FXML
    private void volver() {
        ScreenManager.change("incidencias.fxml");
    }
}