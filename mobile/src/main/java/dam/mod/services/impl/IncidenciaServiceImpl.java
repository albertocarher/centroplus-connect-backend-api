package dam.mod.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import dam.mod.models.Incidencia;
import dam.mod.repositories.IIncidenciaRepository;
import dam.mod.services.IIncidenciaService;
import dam.mod.services.IUsuarioService;
import dam.mod.utils.Validaciones;

public class IncidenciaServiceImpl implements IIncidenciaService {

    private final IIncidenciaRepository repository;
    private final IUsuarioService usuarioService;

    public IncidenciaServiceImpl(IIncidenciaRepository repository,
            IUsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<Incidencia> findAll() {
        return repository.findAll();
    }

    @Override
    public Incidencia findById(int id) {
        return repository.findById(id);
    }

    @Override
    public boolean create(Incidencia incidencia) {
        validar(incidencia);

        if (usuarioService.findById(incidencia.getIdUsuario()) == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        return repository.save(incidencia);
    }

    @Override
    public boolean update(Incidencia incidencia) {
        validar(incidencia);
        return repository.update(incidencia);
    }

    @Override
    public boolean delete(int id) {
        return repository.delete(id);
    }

    @Override
    public boolean cambiarEstado(int idIncidencia, String nuevoEstado) {
        Incidencia incidencia = findById(idIncidencia);
        if (incidencia == null) {
            return false;
        }

        Validaciones.validarEstadoIncidencia(nuevoEstado);
        incidencia.setEstado(nuevoEstado);
        return repository.update(incidencia);
    }

    @Override
    public List<Incidencia> findByUsuario(int idUsuario) {
        return repository.findAll().stream()
                .filter(i -> i.getIdUsuario() == idUsuario)
                .collect(Collectors.toList());
    }

    private void validar(Incidencia incidencia) {
        if (incidencia == null) {
            throw new IllegalArgumentException("Incidencia no puede ser null");
        }

        Validaciones.validarEstadoIncidencia(incidencia.getEstado());
        Validaciones.validarNoVacio(incidencia.getDescripcion(), "Descripción");
    }
}
