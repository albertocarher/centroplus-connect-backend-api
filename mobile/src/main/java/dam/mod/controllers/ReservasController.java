package dam.mod.controllers;

import dam.mod.models.Reserva;
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

/**
 * Controlador de la pantalla de reservas del usuario.
 *
 * Gestiona la visualización, carga y cancelación de reservas del usuario
 * autenticado.
 * Actúa como intermediario entre la vista y la capa de servicios.
 */
public class ReservasController {

    /**
     * Lista visual que muestra las reservas del usuario.
     */
    @FXML
    private ListView<Reserva> listaReservas;

    private IReservaService reservaService;

    /**
     * Inicializa el controlador.
     *
     * Se ejecuta automáticamente al cargar la vista.
     * Crea repositorios, servicios y carga las reservas del usuario.
     */
    @FXML
    public void initialize() {

        /**
         * Si no hay usuario en sesión, redirige al login.
         */
        if (Session.getCurrentUser() == null) {
            ScreenManager.change("login.fxml");
            return;
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

    /**
     * Carga las reservas del usuario autenticado en la lista.
     */
    private void cargarReservas() {

        int idUsuario = Session.getCurrentUser().getId();

        listaReservas.getItems().setAll(
                reservaService.findByIdUsuario(idUsuario));
    }

    /**
     * Cancela la reserva seleccionada.
     *
     * Si la cancelación es correcta, actualiza la lista de reservas.
     */
    @FXML
    private void cancelarReserva() {

        Reserva seleccionada = listaReservas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            return;
        }

        int idUsuario = Session.getCurrentUser().getId();

        if (seleccionada.getIdUsuario() != idUsuario) {
            System.out.println("No tienes permisos para cancelar esta reserva");
            return;
        }

        boolean ok = reservaService.cambiarEstado(
                seleccionada.getId(),
                "CANCELADA");

        if (ok) {
            System.out.println("Reserva cancelada");
            cargarReservas();
        } else {
            System.out.println("No se pudo cancelar");
        }
    }

    /**
     * Vuelve a la pantalla principal.
     */
    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}