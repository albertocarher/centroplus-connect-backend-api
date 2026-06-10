package com.dam.mod.adapters.in.controller;

import com.dam.mod.adapters.in.api.LoginRequest;
import com.dam.mod.adapters.in.api.UsuarioRequest;
import com.dam.mod.adapters.in.api.UsuarioResponse;
import com.dam.mod.business.UsuarioServicePort;
import com.dam.mod.domain.models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioServicePort service;

    public UsuarioController(UsuarioServicePort service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        List<UsuarioResponse> response = service.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable int id) {
        Usuario usuario = service.findById(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(usuario));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<UsuarioResponse> findByDni(@PathVariable String dni) {
        Usuario usuario = service.findByDni(dni);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(usuario));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody UsuarioRequest request) {
        service.save(toDomain(request));
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = service.login(request.getDni(), request.getPassword());
        if (usuario == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(toResponse(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody UsuarioRequest request) {
        Usuario usuario = toDomain(request);
        usuario.setId(id);
        service.update(usuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Usuario toDomain(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setDni(request.getDni());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setTipoUsuario(request.getTipoUsuario());
        usuario.setPassword(request.getPassword());
        return usuario;
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getDni(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getTipoUsuario()
        );
    }
}
