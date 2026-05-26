package dam.mod.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import dam.mod.models.Actividad;
import dam.mod.repositories.IActividadRepository;
import dam.mod.services.IActividadService;
import dam.mod.utils.Validaciones;

public class ActividadServiceImpl implements IActividadService {

    private final IActividadRepository repository;

    public ActividadServiceImpl(IActividadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Actividad> findAll() {
        return repository.findAll();
    }

    @Override
    public Actividad findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean create(Actividad actividad) {
        validar(actividad);
        return repository.save(actividad);
    }

    @Override
    public boolean update(Actividad actividad) {
        validar(actividad);
        return repository.update(actividad);
    }

    @Override
    public boolean delete(int id) {
        return repository.delete(id);
    }

    @Override
    public boolean reservarPlaza(int idActividad) {
        Actividad actividad = findById(idActividad);
        if (actividad == null) {
            return false;
        }
        if (actividad.getPlazasOcupadas() >= actividad.getPlazasMaximas()) {
            return false;
        }

        actividad.setPlazasOcupadas(actividad.getPlazasOcupadas() + 1);
        return repository.update(actividad);
    }

    @Override
    public boolean cancelarPlaza(int idActividad) {
        Actividad actividad = findById(idActividad);
        if (actividad == null) {
            return false;
        }
        if (actividad.getPlazasOcupadas() <= 0) {
            return false;
        }

        actividad.setPlazasOcupadas(actividad.getPlazasOcupadas() - 1);
        return repository.update(actividad);
    }

    @Override
    public List<Actividad> findCompletas() {
        return repository.findAll().stream()
                .filter(a -> a.getPlazasOcupadas() >= a.getPlazasMaximas())
                .collect(Collectors.toList());
    }

    @Override
    public int calcularPlazasDisponibles(int idActividad) {
        Actividad actividad = findById(idActividad);
        if (actividad == null) {
            return 0;
        }
        return actividad.getPlazasMaximas() - actividad.getPlazasOcupadas();
    }

    @Override
    public double calcularIngresosTotales() {
        return repository.findAll().stream()
                .mapToDouble(a -> a.getPlazasOcupadas() * a.getPrecio())
                .sum();
    }

    private void validar(Actividad actividad) {
        if (actividad == null) {
            throw new IllegalArgumentException("Actividad no puede ser null");
        }

        Validaciones.validarTipoActividad(actividad.getTipoActividad());
        Validaciones.validarNoVacio(actividad.getNombre(), "Nombre actividad");
        if (actividad.getDuracion() <= 0) {
            throw new IllegalArgumentException("Duración debe ser mayor a 0");
        }
        if (actividad.getPrecio() < 0) {
            throw new IllegalArgumentException("Precio no puede ser negativo");
        }
        if (actividad.getPlazasMaximas() <= 0) {
            throw new IllegalArgumentException("Plazas máximas deben ser mayores a 0");
        }
        if (actividad.getPlazasOcupadas() < 0 || actividad.getPlazasOcupadas() > actividad.getPlazasMaximas()) {
            throw new IllegalArgumentException("Plazas ocupadas inválidas");
        }
    }
}
