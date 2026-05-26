package dam.mod.services.impl;

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

        Usuario usuario = usuarioService.findById(reserva.getIdUsuario());
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        Actividad actividad = actividadService.findById(reserva.getIdActividad());
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no existe");
        }

        if (actividadService.calcularPlazasDisponibles(actividad.getId()) <= 0) {
            throw new IllegalArgumentException("No hay plazas disponibles para esta actividad");
        }

        if (existeReservaUsuarioActividad(reserva.getIdUsuario(), reserva.getIdActividad())) {
            throw new IllegalArgumentException("El usuario ya tiene una reserva para esta actividad");
        }

        actividadService.reservarPlaza(actividad.getId());

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
    public boolean existeReservaUsuarioActividad(int idUsuario, int idActividad) {
        return repository.findAll().stream()
                .anyMatch(r -> r.getIdUsuario() == idUsuario
                && r.getIdActividad() == idActividad);
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
}
