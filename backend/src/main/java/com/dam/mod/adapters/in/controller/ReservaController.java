package com.dam.mod.adapters.in.controller;

import com.dam.mod.adapters.in.api.ReservaRequest;
import com.dam.mod.adapters.in.api.ReservaResponse;
import com.dam.mod.business.ReservaServicePort;
import com.dam.mod.domain.models.Reserva;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaServicePort service;

    public ReservaController(ReservaServicePort service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> findAll() {
        List<ReservaResponse> response = service.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> findById(@PathVariable int id) {
        Reserva reserva = service.findById(id);
        if (reserva == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(reserva));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReservaResponse>> findByIdUsuario(@PathVariable int idUsuario) {
        List<ReservaResponse> response = service.findByIdUsuario(idUsuario)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ReservaRequest request) {
        service.save(toDomain(request));
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody ReservaRequest request) {
        Reserva reserva = toDomain(request);
        reserva.setId(id);
        service.update(reserva);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable int id, @RequestParam String estado) {
        service.cambiarEstado(id, estado);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Reserva toDomain(ReservaRequest request) {
        Reserva reserva = new Reserva();
        reserva.setIdUsuario(request.getIdUsuario());
        reserva.setIdActividad(request.getIdActividad());
        reserva.setFecha(request.getFecha());
        reserva.setEstado(request.getEstado());
        return reserva;
    }

    private ReservaResponse toResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getIdUsuario(),
                reserva.getIdActividad(),
                reserva.getFecha(),
                reserva.getEstado(),
                reserva.getNombreActividad()
        );
    }
}
