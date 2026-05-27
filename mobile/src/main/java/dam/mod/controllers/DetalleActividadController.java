package dam.mod.controllers;

import dam.mod.models.Actividad;
import dam.mod.models.Usuario;
import dam.mod.repositories.IActividadRepository;
import dam.mod.repositories.IReservaRepository;
import dam.mod.repositories.IUsuarioRepository;
import dam.mod.repositories.impl.ActividadRepository;
import dam.mod.repositories.impl.ReservaRepository;
import dam.mod.repositories.impl.UsuarioRepository;
import dam.mod.services.IActividadService;
import dam.mod.services.IReservaService;
import dam.mod.services.IUsuarioService;
import dam.mod.services.impl.ActividadServiceImpl;
import dam.mod.services.impl.ReservaServiceImpl;
import dam.mod.services.impl.UsuarioServiceImpl;
import dam.mod.utils.ScreenManager;
import dam.mod.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DetalleActividadController {

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblDuracion;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblPlazas;

    private static Actividad actividadSeleccionada;

    private IActividadService actividadService;
    private IReservaService reservaService;

    public static void setActividad(Actividad a) {
        actividadSeleccionada = a;
    }

    @FXML
    public void initialize() {

        // Login obligatorio
        Usuario user = Session.getCurrentUser();
        if (user == null) {
            ScreenManager.change("login.fxml");
            return;
        }

        // SERVICES

        IActividadRepository actividadRepo = new ActividadRepository();
        actividadService = new ActividadServiceImpl(actividadRepo);

        IReservaRepository reservaRepo = new ReservaRepository();

        IUsuarioRepository usuarioRepo = new UsuarioRepository();
        IUsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepo);

        reservaService = new ReservaServiceImpl(
                reservaRepo,
                usuarioService,
                actividadService
        );

        // LOAD DATA
        if (actividadSeleccionada != null) {
            cargarDatos();
        }
    }

    private void cargarDatos() {

        lblNombre.setText("Nombre: " + actividadSeleccionada.getNombre());
        lblTipo.setText("Tipo: " + actividadSeleccionada.getTipoActividad());
        lblDuracion.setText("Duración: " + actividadSeleccionada.getDuracion() + " min");
        lblPrecio.setText("Precio: " + actividadSeleccionada.getPrecio() + " €");

        int disponibles =
                actividadSeleccionada.getPlazasMaximas()
                        - actividadSeleccionada.getPlazasOcupadas();

        lblPlazas.setText("Plazas disponibles: " + disponibles);
    }

    @FXML
    private void reservar() {

        int usuarioId = Session.getCurrentUser().getId();
        int actividadId = actividadSeleccionada.getId();

        // ya reservado
        if (reservaService.yaReservado(actividadId, usuarioId)) {
            System.out.println("Ya tienes esta actividad reservada");
            return;
        }

        // sin plazas
        int disponibles =
                actividadSeleccionada.getPlazasMaximas()
                        - actividadSeleccionada.getPlazasOcupadas();

        if (disponibles <= 0) {
            System.out.println("No hay plazas disponibles");
            return;
        }

        // reservar
        boolean ok = reservaService.reservar(actividadId, usuarioId);

        if (ok) {

            actividadSeleccionada =
                    actividadService.findById(actividadId);

            cargarDatos();

            System.out.println("Reserva realizada correctamente");

        } else {
            System.out.println("Error al realizar la reserva");
        }
    }

    //volver
    @FXML
    private void volver() {
        ScreenManager.change("actividades.fxml");
    }
}