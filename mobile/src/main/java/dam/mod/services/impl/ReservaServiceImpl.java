package dam.mod.services.impl;

import java.time.LocalDate;
import java.util.List;

import dam.mod.models.Actividad;
import dam.mod.models.Reserva;
import dam.mod.models.Usuario;
import dam.mod.repositories.IReservaRepository;
import dam.mod.services.IActividadService;
import dam.mod.services.IReservaService;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.Validaciones;

public class ReservaServiceImpl implements IReservaService {

    private final IReservaRepository repository;
    private final IUsuarioService usuarioService;
    private final IActividadService actividadService;

    public ReservaServiceImpl(IReservaRepository repository,
                              IUsuarioService usuarioService,
                              IActividadService actividadService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.actividadService = actividadService;
    }

    @Override
    public List<Reserva> findAll() {
        return repository.findAll();
    }

    @Override
    public Reserva findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean create(Reserva reserva) {

        validar(reserva);

        if (usuarioService.findById(reserva.getIdUsuario()) == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        if (actividadService.findById(reserva.getIdActividad()) == null) {
            throw new IllegalArgumentException("La actividad no existe");
        }

        if (actividadService.calcularPlazasDisponibles(reserva.getIdActividad()) <= 0) {
            throw new IllegalArgumentException("No hay plazas disponibles");
        }

        if (yaReservado(reserva.getIdActividad(), reserva.getIdUsuario())) {
            throw new IllegalArgumentException("Ya tienes una reserva para esta actividad");
        }

        actividadService.reservarPlaza(reserva.getIdActividad());

        return repository.save(reserva);
    }

    @Override
    public boolean update(Reserva reserva) {
        validar(reserva);
        return repository.update(reserva);
    }

    @Override
    public boolean delete(int id) {
        return repository.delete(id);
    }

    @Override
    public boolean cancelarReserva(int idReserva) {

        Reserva reserva = findById(idReserva);

        if (reserva == null) {
            return false;
        }

        actividadService.cancelarPlaza(reserva.getIdActividad());

        return repository.delete(idReserva);
    }

    @Override
    public boolean yaReservado(int actividadId, int usuarioId) {
        return repository.existsReserva(actividadId, usuarioId);
    }

    @Override
    public boolean existeReservaUsuarioActividad(int idUsuario, int idActividad) {
        return yaReservado(idActividad, idUsuario);
    }

    @Override
    public boolean comprobarPlazasDisponibles(int idActividad) {
        return actividadService.calcularPlazasDisponibles(idActividad) > 0;
    }

    private void validar(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva no puede ser null");
        }
        Validaciones.validarEstadoReserva(reserva.getEstado());
    }
    @Override
    public boolean reservar(int actividadId, int usuarioId) {

        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        Actividad actividad = actividadService.findById(actividadId);
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no existe");
        }

        if (actividadService.calcularPlazasDisponibles(actividadId) <= 0) {
            throw new IllegalArgumentException("No hay plazas disponibles");
        }

        if (yaReservado(actividadId, usuarioId)) {
            throw new IllegalArgumentException("Ya tienes una reserva para esta actividad");
        }

        actividadService.reservarPlaza(actividadId);

        Reserva reserva = new Reserva(
                0,
                usuarioId,
                actividadId,
                LocalDate.now(),
                "ACTIVA"
        );

        return repository.save(reserva);
    }
}