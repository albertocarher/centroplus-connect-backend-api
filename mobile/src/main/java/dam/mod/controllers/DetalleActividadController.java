package dam.mod.controllers;

import dam.mod.models.Actividad;

import dam.mod.repositories.IActividadRepository;
import dam.mod.repositories.impl.ActividadRepository;

import dam.mod.services.IActividadService;
import dam.mod.services.impl.ActividadServiceImpl;

import dam.mod.utils.ScreenManager;

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

    public static void setActividad(Actividad a) {
        actividadSeleccionada = a;
    }

    @FXML
    public void initialize() {

        IActividadRepository repo = new ActividadRepository();

        actividadService = new ActividadServiceImpl(repo);

        if (actividadSeleccionada != null) {
            cargarDatos();
        }
    }

    private void cargarDatos() {

        lblNombre.setText("Nombre: " + actividadSeleccionada.getNombre());
        lblTipo.setText("Tipo: " + actividadSeleccionada.getTipoActividad());
        lblDuracion.setText("Duración: " +
                actividadSeleccionada.getDuracion() + " min");
        lblPrecio.setText("Precio: " +
                actividadSeleccionada.getPrecio() + " €");

        int disponibles =
                actividadSeleccionada.getPlazasMaximas()
                        - actividadSeleccionada.getPlazasOcupadas();

        lblPlazas.setText("Plazas disponibles: " + disponibles);
    }

    @FXML
    private void reservar() {

        boolean ok = actividadService.reservarPlaza(
                actividadSeleccionada.getId()
        );

        if (ok) {

            actividadSeleccionada =
                    actividadService.findById(actividadSeleccionada.getId());

            cargarDatos();

            System.out.println("✔ Reserva realizada correctamente");

        } else {
            System.out.println("❌ No se pudo reservar plaza");
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("actividades.fxml");
    }
}