package dam.mod.controllers;

import dam.mod.models.Reserva;
import dam.mod.models.Usuario;
import dam.mod.utils.Session;
import dam.mod.repositories.IReservaRepository;
import dam.mod.repositories.impl.ReservaRepository;

import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.UsuarioRepository;

import dam.mod.repositories.IActividadRepository;
import dam.mod.repositories.impl.ActividadRepository;

import dam.mod.services.IReservaService;
import dam.mod.services.impl.ReservaServiceImpl;

import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.UsuarioServiceImpl;

import dam.mod.services.IActividadService;
import dam.mod.services.impl.ActividadServiceImpl;

import dam.mod.utils.ScreenManager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ReservasController {

    @FXML
    private ListView<Reserva> listaReservas;

    private IReservaService reservaService;
    private IUsuarioService usuarioService;

    // inicializacion
    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
        }
        IReservaRepository reservaRepo = new ReservaRepository();
        IUsuarioRepository usuarioRepo = new UsuarioRepository();
        IActividadRepository actividadRepo = new ActividadRepository();

        IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepo);

        IActividadService actividadService = new ActividadServiceImpl(actividadRepo);

        reservaService = new ReservaServiceImpl(
                reservaRepo,
                usuarioService,
                actividadService);

        cargarReservas();
    }

    // Carga reservas
    private void cargarReservas() {

        int idUsuario = Session.getCurrentUser().getId();

        listaReservas.getItems().setAll(
                reservaService.findByIdUsuario(idUsuario));
    }

    // cancela reservas
    @FXML
    private void cancelarReserva() {

        Reserva seleccionada = listaReservas.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {

            int idUsuario = Session.getCurrentUser().getId();

            boolean ok = reservaService.cancelarReserva(seleccionada.getId(), idUsuario);

            if (ok) {
                System.out.println("Reserva cancelada");
                cargarReservas();
            } else {
                System.out.println("No se pudo cancelar");
            }
        }
    }

    // volver al inicio
    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}