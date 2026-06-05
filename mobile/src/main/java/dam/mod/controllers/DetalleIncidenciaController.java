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
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.ResourceBundle;

/**
 * Controlador del detalle de una incidencia.
 */
public class DetalleIncidenciaController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private Label lblAsunto;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblFecha;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Button btnCerrar;

    private IIncidenciaService incidenciaService;

    private Incidencia inc;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        bundle = LanguageManager.getBundle();

        IIncidenciaRepository repo = new IncidenciaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(
                usuarioRepo,
                new RememberTokenRepositoryImpl()
        );

        incidenciaService = new IncidenciaServiceImpl(repo, usuarioService);

        cargarIncidencia();
    }

    private void cargarIncidencia() {

        int id = ScreenManager.getIncidenciaId();

        inc = incidenciaService.findById(id);

        if (inc == null) {
            mensajeLabel.setText(bundle.getString("error.incident.notfound"));
            ScreenManager.change("incidencias.fxml");
            return;
        }

        lblAsunto.setText(inc.getAsunto() != null ? inc.getAsunto() : "");

        lblEstado.setText(
                bundle.getString("incident.state") + ": " +
                (inc.getEstado() != null ? inc.getEstado() : bundle.getString("incident.unknown"))
        );

        lblFecha.setText(
                bundle.getString("incident.date") + ": " + inc.getFecha()
        );

        txtDescripcion.setText(
                inc.getDescripcion() != null ? inc.getDescripcion() : ""
        );

        actualizarBoton();
    }

    private void actualizarBoton() {

        if (btnCerrar == null || inc == null) return;

        boolean cerrada = inc.getEstado() != null &&
                inc.getEstado().equalsIgnoreCase("CERRADA");

        btnCerrar.setDisable(cerrada);
    }

    @FXML
    private void cerrarIncidencia() {

        if (inc == null) return;

        boolean ok = incidenciaService.cambiarEstado(inc.getId(), "CERRADA");

        if (ok) {
            mensajeLabel.setText(bundle.getString("success.incident.closed"));

            inc = incidenciaService.findById(inc.getId());
            cargarIncidencia();
        } else {
            mensajeLabel.setText(bundle.getString("error.incident.close"));
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("incidencias.fxml");
    }
}