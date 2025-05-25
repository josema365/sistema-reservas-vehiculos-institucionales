package com.reservas.vehiculos.institucionales.service.impl;

import com.reservas.vehiculos.institucionales.dto.ReparacionDTO;
import com.reservas.vehiculos.institucionales.model.Reparacion;
import com.reservas.vehiculos.institucionales.model.Vehiculo;
import com.reservas.vehiculos.institucionales.repository.ReparacionRepository;
import com.reservas.vehiculos.institucionales.repository.VehiculoRepository;
import com.reservas.vehiculos.institucionales.service.ReparacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReparacionServiceImpl implements ReparacionService {

    private final ReparacionRepository repository;
    private final VehiculoRepository vehiculoRepository;

    public ReparacionServiceImpl(ReparacionRepository repository, VehiculoRepository vehiculoRepository) {
        this.repository = repository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    @Transactional
    public Reparacion crearReparacion(ReparacionDTO dto) {
        Reparacion r = new Reparacion();

        Optional<Vehiculo> optionalVehiculo = vehiculoRepository.findById(dto.getIdAuto());

        if (optionalVehiculo.isEmpty()) {
            throw new IllegalArgumentException("Vehiculo con ID " + dto.getIdAuto() + " no encontrado para la reparación.");
        }
        Vehiculo vehiculoExistente = optionalVehiculo.get();

        r.setVehiculo(vehiculoExistente);
        r.setCosto(dto.getCosto());
        r.setDescripcion(dto.getDescripcion());
        r.setDocFactura(dto.getDocFactura());
        r.setFechaReparacion(LocalDateTime.now());

        return repository.save(r);
    }

    @Override
    public List<Reparacion> listarReparacionesPorAuto(Long idAuto) {
        return repository.findByVehiculo_Id(idAuto);
    }
}