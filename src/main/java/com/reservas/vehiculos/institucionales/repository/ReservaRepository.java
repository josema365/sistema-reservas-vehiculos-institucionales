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


    List<Reserva> findByUsuario(Usuario usuario);


    @Query("SELECT r FROM Reserva r JOIN r.vehiculos v WHERE v = :vehiculo")
    List<Reserva> findByVehiculo(@Param("vehiculo") Vehiculo vehiculo);


    List<Reserva> findByFechaInicioBetweenOrFechaFinBetween(
                                                             LocalDateTime startDate1, LocalDateTime endDate1,
                                                             LocalDateTime startDate2, LocalDateTime endDate2);


    @Query("SELECT r FROM Reserva r WHERE :currentDate BETWEEN r.fechaInicio AND r.fechaFin")
    List<Reserva> findActiveReservas(@Param("currentDate") LocalDateTime currentDate);


    @Query("SELECT COUNT(r) > 0 FROM Reserva r JOIN r.vehiculos v " +
            "WHERE v.id = :vehiculoId AND " +
            "((r.fechaInicio BETWEEN :startDate AND :endDate) OR " +
            "(r.fechaFin BETWEEN :startDate AND :endDate) OR " +
            "(:startDate BETWEEN r.fechaInicio AND r.fechaFin) OR " +
            "(:endDate BETWEEN r.fechaInicio AND r.fechaFin))")
    boolean existsReservaForVehiculoInPeriod(
            @Param("vehiculoId") Long vehiculoId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}