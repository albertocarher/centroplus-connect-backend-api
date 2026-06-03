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

import dam.mod.utils.ScreenManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Controlador de la pantalla de incidencias del usuario.
 *
 * Permite crear incidencias, listarlas, seleccionarlas y navegar al detalle.
 * Gestiona la comunicación entre la vista y la capa de servicios.
 */
public class IncidenciasController {

    @FXML
    private Label mensajeLabel;
    /**
     * Campo de asunto de la incidencia.
     */
    @FXML
    private TextField txtAsunto;

    /**
     * Campo de descripción de la incidencia.
     */
    @FXML
    private TextArea txtDescripcion;

    /**
     * Lista visual de incidencias del usuario.
     */
    @FXML
    private ListView<Incidencia> listaIncidencias;

    private IIncidenciaService incidenciaService;

    private Usuario usuarioActual;

    /**
     * Inicializa el controlador.
     *
     * Verifica sesión activa, inicializa servicios y carga incidencias del usuario.
     */
    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        usuarioActual = Session.getCurrentUser();

        IIncidenciaRepository repo = new IncidenciaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepo, new RememberTokenRepositoryImpl());

        incidenciaService = new IncidenciaServiceImpl(repo, usuarioService);

        cargarIncidencias();
    }

    /**
     * Carga las incidencias del usuario en la lista visual.
     */
    private void cargarIncidencias() {

        listaIncidencias.getItems().setAll(
                incidenciaService.findByUsuario(usuarioActual.getId()));
    }

    /**
     * Envía una nueva incidencia al sistema.
     *
     * Valida campos, crea la incidencia y actualiza la lista.
     */
    @FXML
    private void enviarIncidencia() {

        String asunto = txtAsunto.getText();
        String descripcion = txtDescripcion.getText();

        if (asunto.isBlank() || descripcion.isBlank()) {
            mensajeLabel.setText("Campos vacíos");
            return;
        }

        Incidencia incidencia = new Incidencia(
                0,
                usuarioActual.getId(),
                asunto,
                descripcion,
                LocalDate.now(),
                "ABIERTA");

        boolean ok = incidenciaService.create(incidencia);

        if (ok) {
            mensajeLabel.setText("Incidencia enviada");
            cargarIncidencias();
        } else {
            mensajeLabel.setText("Error al enviar incidencia");
        }

        txtAsunto.clear();
        txtDescripcion.clear();
    }

    /**
     * Selecciona una incidencia y abre su vista de detalle.
     */
    @FXML
    private void seleccionarIncidencia(javafx.scene.input.MouseEvent event) {

        Incidencia incidencia = listaIncidencias.getSelectionModel().getSelectedItem();

        if (incidencia == null)
            return;

        ScreenManager.setIncidenciaId(incidencia.getId());
        ScreenManager.change("detalle_incidencia.fxml");
    }

    /**
     * Vuelve a la pantalla principal.
     */
    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}