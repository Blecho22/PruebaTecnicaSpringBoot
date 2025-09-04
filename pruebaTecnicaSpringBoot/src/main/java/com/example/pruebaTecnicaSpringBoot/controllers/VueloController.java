package com.example.pruebaTecnicaSpringBoot.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pruebaTecnicaSpringBoot.dtos.VueloActualizacionDTO;
import com.example.pruebaTecnicaSpringBoot.dtos.VueloDTO;
import com.example.pruebaTecnicaSpringBoot.services.VueloService;

import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/vuelos")
public class VueloController {

    private final VueloService vueloService;

    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    // GET /api/vuelos?empresa&lugarLlegada&fechaSalida&ordenarPor
    @GetMapping
    public ResponseEntity<List<VueloDTO>> listar(
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String lugarLlegada,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida,
            @RequestParam(required = false, defaultValue = "fechaSalida") String ordenarPor) {

        List<VueloDTO> respuesta = vueloService.listar(empresa, lugarLlegada, fechaSalida, ordenarPor);
        return ResponseEntity.ok(respuesta);
    }

    // GET /api/vuelos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VueloDTO> obtener(@PathVariable int id) {
        return ResponseEntity.ok(vueloService.obtenerPorId(id));
    }

    //POST /api/vuelos
    @PostMapping
    public ResponseEntity<VueloDTO> crear(@Valid @RequestBody VueloActualizacionDTO dto,
                                          UriComponentsBuilder uriBuilder) {
        VueloDTO creado = vueloService.crear(dto);
        return ResponseEntity
                .created(uriBuilder.path("/api/vuelos/{id}").buildAndExpand(creado.getId()).toUri())
                .body(creado);
    }

    // PUT /api/vuelos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<VueloDTO> actualizar(@PathVariable int id,
                                               @Valid @RequestBody VueloActualizacionDTO dto) {
        return ResponseEntity.ok(vueloService.actualizar(id, dto));
    }

    // DELETE /api/vuelos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        vueloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // 404 para entidades no encontradas
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "NOT_FOUND");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(404).body(body);
    }

    // 400 para reglas de negocio inválidas
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "BAD_REQUEST");
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    // 400 en el request body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("error", "VALIDATION_FAILED");
        body.put("message", "Datos inválidos");
        body.put("fields", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }
}

