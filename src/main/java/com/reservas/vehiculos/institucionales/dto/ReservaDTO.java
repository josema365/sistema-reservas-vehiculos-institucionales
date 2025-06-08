package com.reservas.vehiculos.institucionales.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    
    private Long id;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio debe ser actual o futura")
    private LocalDateTime fechaInicio;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin debe ser actual o futura")
    private LocalDateTime fechaFin;
    
    private LocalDateTime fechaReserva;
    
    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;
    
    @NotEmpty(message = "Debe seleccionar al menos un vehículo")
    private List<Long> vehiculoIds;

    private EstadoReserva estado;
} 