package dam.mod.controllers;

import dam.mod.models.Incidencia;
import dam.mod.models.Usuario;
import dam.mod.utils.Session;
import dam.mod.repositories.IIncidenciaRepository;
import dam.mod.repositories.impl.IncidenciaRepository;
import dam.mod.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.UsuarioRepository;

import dam.mod.services.IIncidenciaService;
import dam.mod.services.impl.IncidenciaServiceImpl;

import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de incidencias del usuario.
 */
public class IncidenciasController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private TextField txtAsunto;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private ListView<Incidencia> listaIncidencias;

    private IIncidenciaService incidenciaService;

    private Usuario usuarioActual;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        bundle = LanguageManager.getBundle();

        usuarioActual = Session.getCurrentUser();

        IIncidenciaRepository repo = new IncidenciaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(
                usuarioRepo,
                new RememberTokenRepositoryImpl()
        );

        incidenciaService = new IncidenciaServiceImpl(repo, usuarioService);

        cargarIncidencias();
    }

    private void cargarIncidencias() {

        listaIncidencias.getItems().setAll(
                incidenciaService.findByUsuario(usuarioActual.getId())
        );
    }

    @FXML
    private void enviarIncidencia() {

        String asunto = txtAsunto.getText();
        String descripcion = txtDescripcion.getText();

        if (asunto.isBlank() || descripcion.isBlank()) {
            mensajeLabel.setText(bundle.getString("error.fields.empty"));
            return;
        }

        Incidencia incidencia = new Incidencia(
                0,
                usuarioActual.getId(),
                asunto,
                descripcion,
                LocalDate.now(),
                "ABIERTA"
        );

        boolean ok = incidenciaService.create(incidencia);

        if (ok) {
            mensajeLabel.setText(bundle.getString("success.incident.sent"));
            cargarIncidencias();
        } else {
            mensajeLabel.setText(bundle.getString("error.incident.send"));
        }

        txtAsunto.clear();
        txtDescripcion.clear();
    }

    @FXML
    private void seleccionarIncidencia(javafx.scene.input.MouseEvent event) {

        Incidencia incidencia = listaIncidencias.getSelectionModel().getSelectedItem();

        if (incidencia == null) return;

        ScreenManager.setIncidenciaId(incidencia.getId());
        ScreenManager.change("detalle_incidencia.fxml");
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}