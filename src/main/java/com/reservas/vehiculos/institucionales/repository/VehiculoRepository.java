package com.reservas.vehiculos.institucionales.repository;

import com.reservas.vehiculos.institucionales.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPlaca(String placa);
    List<Vehiculo> findByMarca(String marca);
    List<Vehiculo> findByTipo(String tipo);
    boolean existsByPlaca(String placa);

    @Query("SELECT v FROM Vehiculo v WHERE v NOT IN (SELECT r.vehiculos FROM Reserva r WHERE :fecha BETWEEN r.fechaInicio AND r.fechaFin)") // <-- ¡CAMBIO AQUÍ!
    List<Vehiculo> findAvailableVehiculos(LocalDateTime fecha);
}