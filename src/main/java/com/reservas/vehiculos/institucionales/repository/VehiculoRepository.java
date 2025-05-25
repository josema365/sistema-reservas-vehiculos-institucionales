package com.reservas.vehiculos.institucionales.repository;


import com.reservas.vehiculos.institucionales.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Aquí puedes agregar consultas personalizadas si necesitas
}
