package com.reservas.vehiculos.institucionales.validation;

import com.reservas.vehiculos.institucionales.dto.ReservaDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ReservaValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ReservaDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservaDTO reservaDTO = (ReservaDTO) target;
        
        // Validar que la fecha de fin sea posterior a la fecha de inicio
        if (reservaDTO.getFechaInicio() != null && reservaDTO.getFechaFin() != null) {
            if (reservaDTO.getFechaFin().isBefore(reservaDTO.getFechaInicio()) || 
                    reservaDTO.getFechaFin().isEqual(reservaDTO.getFechaInicio())) {
                errors.rejectValue("fechaFin", "error.fechas", 
                        "La fecha de fin debe ser posterior a la fecha de inicio");
            }
        }
        
        // Validar que la duración de la reserva no sea excesiva (ejemplo: máximo 7 días)
        if (reservaDTO.getFechaInicio() != null && reservaDTO.getFechaFin() != null) {
            long dias = java.time.Duration.between(
                    reservaDTO.getFechaInicio(), reservaDTO.getFechaFin()).toDays();
            if (dias > 7) {
                errors.rejectValue("fechaFin", "error.duracion", 
                        "La duración máxima de una reserva es de 7 días");
            }
        }
    }
} 