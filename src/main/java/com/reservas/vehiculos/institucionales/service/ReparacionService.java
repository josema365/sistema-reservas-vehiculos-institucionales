package com.reservas.vehiculos.institucionales.service;

import com.reservas.vehiculos.institucionales.dto.ReparacionDTO;
import com.reservas.vehiculos.institucionales.model.Reparacion;

import java.util.List;

public interface ReparacionService {
    Reparacion crearReparacion(ReparacionDTO dto);
    List<Reparacion> listarReparacionesPorAuto(Long idAuto);
}