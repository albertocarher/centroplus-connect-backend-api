package com.dam.mod.adapters.in.controller;

import com.dam.mod.adapters.in.api.IncidenciaRequest;
import com.dam.mod.adapters.in.api.IncidenciaResponse;
import com.dam.mod.business.IncidenciaServicePort;
import com.dam.mod.domain.models.Incidencia;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/incidencias")
public class IncidenciaController {

    private final IncidenciaServicePort service;

    public IncidenciaController(IncidenciaServicePort service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<IncidenciaResponse>> findAll() {
        List<IncidenciaResponse> response = service.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaResponse> findById(@PathVariable int id) {
        Incidencia incidencia = service.findById(id);
        if (incidencia == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(incidencia));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody IncidenciaRequest request) {
        service.save(toDomain(request));
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody IncidenciaRequest request) {
        Incidencia incidencia = toDomain(request);
        incidencia.setId(id);
        service.update(incidencia);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Incidencia toDomain(IncidenciaRequest request) {
        Incidencia incidencia = new Incidencia();
        incidencia.setIdUsuario(request.getIdUsuario());
        incidencia.setAsunto(request.getAsunto());
        incidencia.setDescripcion(request.getDescripcion());
        incidencia.setFecha(request.getFecha());
        incidencia.setEstado(request.getEstado());
        return incidencia;
    }

    private IncidenciaResponse toResponse(Incidencia incidencia) {
        return new IncidenciaResponse(
                incidencia.getId(),
                incidencia.getIdUsuario(),
                incidencia.getAsunto(),
                incidencia.getDescripcion(),
                incidencia.getFecha(),
                incidencia.getEstado()
        );
    }
}
