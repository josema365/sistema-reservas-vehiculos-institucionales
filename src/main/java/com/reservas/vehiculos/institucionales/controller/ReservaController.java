package com.reservas.vehiculos.institucionales.controller;

import com.reservas.vehiculos.institucionales.dto.ReservaDTO;
import com.reservas.vehiculos.institucionales.service.ReservaService;
import com.reservas.vehiculos.institucionales.validation.ReservaValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Cacheable(value = "reservas", key = "'allReservas'")
    public ResponseEntity<List<ReservaDTO>> getAllReservas() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Cacheable(value = "reservas", key = "#id")
    public ResponseEntity<ReservaDTO> getReservaById(@PathVariable Long id) {
        Optional<ReservaDTO> reserva = reservaService.findById(id);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN') or #usuarioId == authentication.principal.id")
    @Cacheable(value = "reservas", key = "'reservasUsuario_' + #usuarioId")
    public ResponseEntity<List<ReservaDTO>> getReservasByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(reservaService.findByUsuarioId(usuarioId));
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Cacheable(value = "reservas", key = "'reservasVehiculo_' + #vehiculoId")
    public ResponseEntity<List<ReservaDTO>> getReservasByVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(reservaService.findByVehiculoId(vehiculoId));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Cacheable(value = "reservas", key = "'reservasActivas'")
    public ResponseEntity<List<ReservaDTO>> getActiveReservas() {
        return ResponseEntity.ok(reservaService.findActiveReservas());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @CacheEvict(value = "reservas", allEntries = true)
    public ResponseEntity<?> createReserva(@Valid @RequestBody ReservaDTO reservaDTO, BindingResult result) {
        reservaValidation.validate(reservaDTO, result);
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatErrors(result));
        }

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
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "reservas", allEntries = true)
    public ResponseEntity<?> updateReserva(
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO reservaDTO,
            BindingResult result) {
        
        Optional<ReservaDTO> existingReserva = reservaService.findById(id);
        if (existingReserva.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        reservaValidation.validate(reservaDTO, result);
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatErrors(result));
        }

        List<Long> existingVehiculoIds = existingReserva.get().getVehiculoIds();
        List<Long> newVehiculoIds = reservaDTO.getVehiculoIds();

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
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "reservas", allEntries = true)
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        Optional<ReservaDTO> existingReserva = reservaService.findById(id);
        if (existingReserva.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehiculo/{vehiculoId}/disponibilidad")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Map<String, Boolean>> checkVehiculoAvailability(
            @PathVariable Long vehiculoId,
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        
        boolean disponible = reservaService.isVehiculoAvailable(vehiculoId, fechaInicio, fechaFin);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("disponible", disponible);
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "reservas", allEntries = true)
    public ResponseEntity<?> updateReservaEstado(
            @PathVariable Long id,
            @RequestParam EstadoReserva estado) {
        
        Optional<ReservaDTO> existingReserva = reservaService.findById(id);
        if (existingReserva.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ReservaDTO reservaDTO = existingReserva.get();
        reservaDTO.setEstado(estado);
        
        ReservaDTO updatedReserva = reservaService.update(id, reservaDTO);
        return ResponseEntity.ok(updatedReserva);
    }

    private Map<String, String> formatErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
} 