package com.reservas.vehiculos.institucionales.controller;

import com.reservas.vehiculos.institucionales.dto.ReparacionDTO;
import com.reservas.vehiculos.institucionales.model.Reparacion;
import com.reservas.vehiculos.institucionales.service.ReparacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reparaciones")
public class ReparacionController {

    private final ReparacionService service;

    public ReparacionController(ReparacionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Reparacion> crear(@RequestBody ReparacionDTO dto) {
        return ResponseEntity.ok(service.crearReparacion(dto));
    }

    @GetMapping("/auto/{id}")
    public ResponseEntity<List<Reparacion>> listar(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarReparacionesPorAuto(id));
    }
}
