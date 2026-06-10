package com.dam.mod.adapters.in.controller;

import com.dam.mod.adapters.in.api.ActividadRequest;
import com.dam.mod.adapters.in.api.ActividadResponse;
import com.dam.mod.business.ActividadServicePort;
import com.dam.mod.domain.models.Actividad;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    private final ActividadServicePort service;

    public ActividadController(ActividadServicePort service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ActividadResponse>> findAll() {
        List<ActividadResponse> response = service.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> findById(@PathVariable int id) {
        Actividad actividad = service.findById(id);
        if (actividad == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(actividad));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ActividadRequest request) {
        service.save(toDomain(request));
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody ActividadRequest request) {
        Actividad actividad = toDomain(request);
        actividad.setId(id);
        service.update(actividad);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Actividad toDomain(ActividadRequest request) {
        Actividad actividad = new Actividad();
        actividad.setNombre(request.getNombre());
        actividad.setTipoActividad(request.getTipoActividad());
        actividad.setDuracion(request.getDuracion());
        actividad.setPrecio(request.getPrecio());
        actividad.setPlazasMaximas(request.getPlazasMaximas());
        actividad.setPlazasOcupadas(request.getPlazasOcupadas());
        return actividad;
    }

    private ActividadResponse toResponse(Actividad actividad) {
        return new ActividadResponse(
                actividad.getId(),
                actividad.getNombre(),
                actividad.getTipoActividad(),
                actividad.getDuracion(),
                actividad.getPrecio(),
                actividad.getPlazasMaximas(),
                actividad.getPlazasOcupadas()
        );
    }
}
