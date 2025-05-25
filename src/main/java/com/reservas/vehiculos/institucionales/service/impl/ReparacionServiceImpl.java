package com.reservas.vehiculos.institucionales.service.impl;

import com.reservas.vehiculos.institucionales.dto.ReparacionDTO;
import com.reservas.vehiculos.institucionales.model.Reparacion;
import com.reservas.vehiculos.institucionales.model.Vehiculo;
import com.reservas.vehiculos.institucionales.repository.ReparacionRepository;
import com.reservas.vehiculos.institucionales.service.ReparacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReparacionServiceImpl implements ReparacionService {

    private final ReparacionRepository repository;

    public ReparacionServiceImpl(ReparacionRepository repository) {
        this.repository = repository;
    }

   @Override
public Reparacion crearReparacion(ReparacionDTO dto) {
    Reparacion r = new Reparacion();
    Vehiculo vehiculo = new Vehiculo();
    vehiculo.setId(dto.getIdAuto());
    r.setVehiculo(vehiculo);
    r.setCosto(dto.getCosto());
    r.setDescripcion(dto.getDescripcion());
    r.setDocFactura(dto.getDocFactura());
    return repository.save(r);
}

    @Override
    public List<Reparacion> listarReparacionesPorAuto(Long idAuto) {
        return repository.findByVehiculo_Id(idAuto);
    }
}
