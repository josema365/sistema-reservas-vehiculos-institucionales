package com.reservas.vehiculos.institucionales.service;

import com.reservas.vehiculos.institucionales.dto.ReservaDTO;
import com.reservas.vehiculos.institucionales.model.Reserva;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaService {
    
    List<ReservaDTO> findAll();
    
    Optional<ReservaDTO> findById(Long id);
    
    List<ReservaDTO> findByUsuarioId(Long usuarioId);
    
    List<ReservaDTO> findByVehiculoId(Long vehiculoId);
    
    List<ReservaDTO> findActiveReservas();
    
    ReservaDTO save(ReservaDTO reservaDTO);
    
    ReservaDTO update(Long id, ReservaDTO reservaDTO);
    
    void delete(Long id);
    
    boolean isVehiculoAvailable(Long vehiculoId, LocalDateTime startDate, LocalDateTime endDate);
    
    boolean areAllVehiculosAvailable(List<Long> vehiculosIds, LocalDateTime startDate, LocalDateTime endDate);
} 