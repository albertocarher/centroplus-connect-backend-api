package dam.mod.controllers;

import dam.mod.models.Actividad;

import dam.mod.repositories.IActividadRepository;
import dam.mod.repositories.impl.ActividadRepository;

import dam.mod.services.IActividadService;
import dam.mod.services.impl.ActividadServiceImpl;

import dam.mod.utils.ScreenManager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ActividadesController {

    @FXML
    private ListView<Actividad> listaActividades;

    private IActividadService actividadService;

    @FXML
    public void initialize() {

        IActividadRepository repo = new ActividadRepository();

        actividadService = new ActividadServiceImpl(repo);

        listaActividades.getItems().setAll(
                actividadService.findAll()
        );
    }

    @FXML
    private void seleccionarActividad() {

        Actividad seleccionada =
                listaActividades.getSelectionModel().getSelectedItem();

        if (seleccionada != null) {

            DetalleActividadController.setActividad(seleccionada);

            ScreenManager.change("detalle_actividad.fxml");
        }
    }

    @FXML
    private void volver() {
        ScreenManager.change("inicio.fxml");
    }
}