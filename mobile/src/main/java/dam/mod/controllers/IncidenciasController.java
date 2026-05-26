package dam.mod.controllers;

import dam.mod.models.Incidencia;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class IncidenciasController {

    @FXML
    private TextField txtAsunto;

    @FXML
    private TextArea txtDescripcion;

    private IIncidenciaService incidenciaService;

    private int idUsuario = 1;

    @FXML
    public void initialize() {

        IIncidenciaRepository repo = new IncidenciaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();

        IUsuarioService usuarioService =
                new UsuarioServiceImpl(usuarioRepo);

        incidenciaService =
                new IncidenciaServiceImpl(repo, usuarioService);
    }

    @FXML
    private void enviarIncidencia() {

        String asunto = txtAsunto.getText();
        String descripcion = txtDescripcion.getText();

        if (asunto.isBlank() || descripcion.isBlank()) {
            System.out.println("❌ Campos vacíos");
            return;
        }

        Incidencia incidencia = new Incidencia(
                0,
                idUsuario,
                asunto,
                descripcion,
                LocalDate.now(),
                "ABIERTA"
        );

        boolean ok = incidenciaService.create(incidencia);

        if (ok) {
            System.out.println("✔ Incidencia enviada");

            txtAsunto.clear();
            txtDescripcion.clear();
        } else {
            System.out.println("❌ Error al enviar incidencia");
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}