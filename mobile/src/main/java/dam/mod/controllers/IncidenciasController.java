package dam.mod.controllers;

import dam.mod.models.Incidencia;
import dam.mod.models.Usuario;
import dam.mod.utils.Session;
import dam.mod.repositories.IIncidenciaRepository;
import dam.mod.repositories.impl.IncidenciaRepository;

import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.UsuarioRepository;

import dam.mod.services.IIncidenciaService;
import dam.mod.services.impl.IncidenciaServiceImpl;

import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;

import dam.mod.utils.ScreenManager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class IncidenciasController {

    @FXML
    private TextField txtAsunto;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private ListView<Incidencia> listaIncidencias;

    private IIncidenciaService incidenciaService;

    private Usuario usuarioActual;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        usuarioActual = Session.getCurrentUser();

        IIncidenciaRepository repo = new IncidenciaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepo);

        incidenciaService = new IncidenciaServiceImpl(repo, usuarioService);

        cargarIncidencias();
    }

    private void cargarIncidencias() {
        listaIncidencias.getItems().setAll(
                incidenciaService.findByUsuario(usuarioActual.getId()));
    }

    @FXML
    private void enviarIncidencia() {

        String asunto = txtAsunto.getText();
        String descripcion = txtDescripcion.getText();

        if (asunto.isBlank() || descripcion.isBlank()) {
            System.out.println("Campos vacíos");
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
            System.out.println("Incidencia enviada");
            cargarIncidencias();
        } else {
            System.out.println("Error al enviar incidencia");
        }

        txtAsunto.clear();
        txtDescripcion.clear();
    }

    @FXML
    private void seleccionarIncidencia(javafx.scene.input.MouseEvent event) {

        Incidencia incidencia = listaIncidencias.getSelectionModel().getSelectedItem();

        if (incidencia == null)
            return;

        ScreenManager.setIncidenciaId(incidencia.getId());
        ScreenManager.change("detalle_incidencia.fxml");
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}