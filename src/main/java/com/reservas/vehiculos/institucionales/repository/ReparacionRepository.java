package com.reservas.vehiculos.institucionales.repository;

import com.reservas.vehiculos.institucionales.model.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {
   List<Reparacion> findByVehiculo_Id(Long vehiculoId);

}