package dam.mod.controllers;

import dam.mod.models.Reserva;
import dam.mod.utils.Session;
import dam.mod.repositories.IReservaRepository;
import dam.mod.repositories.impl.ReservaRepository;

import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.UsuarioRepository;

import dam.mod.repositories.IActividadRepository;
import dam.mod.repositories.impl.ActividadRepository;
import dam.mod.repositories.impl.RememberTokenRepositoryImpl;
import dam.mod.services.IReservaService;
import dam.mod.services.impl.ReservaServiceImpl;

import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;

import dam.mod.services.IActividadService;
import dam.mod.services.impl.ActividadServiceImpl;
import dam.mod.utils.LanguageManager;
import dam.mod.utils.ScreenManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

/**
 * Controlador de la pantalla de reservas del usuario.
 */
public class ReservasController {

    @FXML
    private Label mensajeLabel;

    @FXML
    private ListView<Reserva> listaReservas;

    private IReservaService reservaService;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        bundle = LanguageManager.getBundle();

        IReservaRepository reservaRepo = new ReservaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();
        IActividadRepository actividadRepo = new ActividadRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(
                usuarioRepo,
                new RememberTokenRepositoryImpl()
        );

        IActividadService actividadService = new ActividadServiceImpl(actividadRepo);

        reservaService = new ReservaServiceImpl(
                reservaRepo,
                usuarioService,
                actividadService
        );

        cargarReservas();
    }

    private void cargarReservas() {

        int idUsuario = Session.getCurrentUser().getId();

        listaReservas.getItems().setAll(
                reservaService.findByIdUsuario(idUsuario)
        );
    }

    @FXML
    private void cancelarReserva() {

        Reserva seleccionada = listaReservas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) return;

        int idUsuario = Session.getCurrentUser().getId();

        if (seleccionada.getIdUsuario() != idUsuario) {
            mensajeLabel.setText(bundle.getString("error.reservation.permission"));
            return;
        }

        boolean ok = reservaService.cambiarEstado(
                seleccionada.getId(),
                "CANCELADA"
        );

        if (ok) {
            mensajeLabel.setText(bundle.getString("success.reservation.cancelled"));
            cargarReservas();
        } else {
            mensajeLabel.setText(bundle.getString("error.reservation.cancel"));
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}