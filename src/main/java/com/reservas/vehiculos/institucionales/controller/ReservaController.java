package com.reservas.vehiculos.institucionales.controller;

import com.reservas.vehiculos.institucionales.dto.ReservaDTO;
import com.reservas.vehiculos.institucionales.service.ReservaService;
import com.reservas.vehiculos.institucionales.validation.ReservaValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaValidation reservaValidation;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> getAllReservas() {
        List<ReservaDTO> reservas = reservaService.findAll();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getReservaById(@PathVariable Long id) {
        Optional<ReservaDTO> reserva = reservaService.findById(id);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDTO>> getReservasByUsuario(@PathVariable Long usuarioId) {
        List<ReservaDTO> reservas = reservaService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<ReservaDTO>> getReservasByVehiculo(@PathVariable Long vehiculoId) {
        List<ReservaDTO> reservas = reservaService.findByVehiculoId(vehiculoId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<ReservaDTO>> getActiveReservas() {
        List<ReservaDTO> reservas = reservaService.findActiveReservas();
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    public ResponseEntity<?> createReserva(@Valid @RequestBody ReservaDTO reservaDTO, BindingResult result) {
        // Aplicar validación personalizada
        reservaValidation.validate(reservaDTO, result);
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatErrors(result));
        }
        
        // Verificar disponibilidad de vehículos
        if (!reservaService.areAllVehiculosAvailable(
                reservaDTO.getVehiculoIds(), 
                reservaDTO.getFechaInicio(), 
                reservaDTO.getFechaFin())) {
            Map<String, String> error = new HashMap<>();
            error.put("vehiculoIds", "Uno o más vehículos no están disponibles en el período seleccionado");
            return ResponseEntity.badRequest().body(error);
        }

        ReservaDTO savedReserva = reservaService.save(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO reservaDTO,
            BindingResult result) {
        
        // Verificar si existe la reserva
        Optional<ReservaDTO> existingReserva = reservaService.findById(id);
        if (existingReserva.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Aplicar validación personalizada
        reservaValidation.validate(reservaDTO, result);
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatErrors(result));
        }
        
        // Verificar disponibilidad de vehículos (solo para vehículos diferentes a los que ya estaban en la reserva)
        List<Long> existingVehiculoIds = existingReserva.get().getVehiculoIds();
        List<Long> newVehiculoIds = reservaDTO.getVehiculoIds();
        
        // Filtrar solo los nuevos vehículos para verificar disponibilidad
        List<Long> vehiculosToCheck = newVehiculoIds.stream()
                .filter(vehiculoId -> !existingVehiculoIds.contains(vehiculoId))
                .toList();
        
        if (!vehiculosToCheck.isEmpty() && !reservaService.areAllVehiculosAvailable(
                vehiculosToCheck, reservaDTO.getFechaInicio(), reservaDTO.getFechaFin())) {
            Map<String, String> error = new HashMap<>();
            error.put("vehiculoIds", "Uno o más vehículos no están disponibles en el período seleccionado");
            return ResponseEntity.badRequest().body(error);
        }
        
        ReservaDTO updatedReserva = reservaService.update(id, reservaDTO);
        return ResponseEntity.ok(updatedReserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        Optional<ReservaDTO> existingReserva = reservaService.findById(id);
        if (existingReserva.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehiculo/{vehiculoId}/disponibilidad")
    public ResponseEntity<Map<String, Boolean>> checkVehiculoAvailability(
            @PathVariable Long vehiculoId,
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        
        boolean disponible = reservaService.isVehiculoAvailable(vehiculoId, fechaInicio, fechaFin);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("disponible", disponible);
        
        return ResponseEntity.ok(response);
    }

    private Map<String, String> formatErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
} 