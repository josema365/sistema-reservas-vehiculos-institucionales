package com.reservas.vehiculos.institucionales.repository;

import com.reservas.vehiculos.institucionales.model.Reserva;
import com.reservas.vehiculos.institucionales.model.Usuario;
import com.reservas.vehiculos.institucionales.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Buscar reservas por usuario
    List<Reserva> findByUsuario(Usuario usuario);
    
    // Buscar reservas por vehículo
    @Query("SELECT r FROM Reserva r JOIN r.vehiculos v WHERE v = :vehiculo")
    List<Reserva> findByVehiculo(@Param("vehiculo") Vehiculo vehiculo);
    
    // Buscar reservas por rango de fechas
    List<Reserva> findByFecha_inicioBetweenOrFecha_finBetween(
            LocalDateTime startDate1, LocalDateTime endDate1,
            LocalDateTime startDate2, LocalDateTime endDate2);
    
    // Buscar reservas activas (fecha actual entre fecha de inicio y fin)
    @Query("SELECT r FROM Reserva r WHERE :currentDate BETWEEN r.fecha_inicio AND r.fecha_fin")
    List<Reserva> findActiveReservas(@Param("currentDate") LocalDateTime currentDate);
    
    // Verificar si existe una reserva para un vehículo en un período específico
    @Query("SELECT COUNT(r) > 0 FROM Reserva r JOIN r.vehiculos v " +
            "WHERE v.id = :vehiculoId AND " +
            "((r.fecha_inicio BETWEEN :startDate AND :endDate) OR " +
            "(r.fecha_fin BETWEEN :startDate AND :endDate) OR " +
            "(:startDate BETWEEN r.fecha_inicio AND r.fecha_fin) OR " +
            "(:endDate BETWEEN r.fecha_inicio AND r.fecha_fin))")
    boolean existsReservaForVehiculoInPeriod(
            @Param("vehiculoId") Long vehiculoId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
} 