package com.reservas.vehiculos.institucionales.service.impl;

import com.reservas.vehiculos.institucionales.dto.ReservaDTO;
import com.reservas.vehiculos.institucionales.model.Reserva;
import com.reservas.vehiculos.institucionales.model.Usuario;
import com.reservas.vehiculos.institucionales.model.Vehiculo;
import com.reservas.vehiculos.institucionales.repository.ReservaRepository;
import com.reservas.vehiculos.institucionales.repository.UsuarioRepository;
import com.reservas.vehiculos.institucionales.repository.VehiculoRepository;
import com.reservas.vehiculos.institucionales.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    public List<ReservaDTO> findAll() {
        return reservaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservaDTO> findById(Long id) {
        return reservaRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<ReservaDTO> findByUsuarioId(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            return reservaRepository.findByUsuario(usuario.get()).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ReservaDTO> findByVehiculoId(Long vehiculoId) {
        Optional<Vehiculo> vehiculo = vehiculoRepository.findById(vehiculoId);
        if (vehiculo.isPresent()) {
            return reservaRepository.findByVehiculo(vehiculo.get()).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ReservaDTO> findActiveReservas() {
        LocalDateTime now = LocalDateTime.now();
        return reservaRepository.findActiveReservas(now).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservaDTO save(ReservaDTO reservaDTO) {
        Reserva reserva = convertToEntity(reservaDTO);
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setEstado(EstadoReserva.RESERVADO);
        reserva = reservaRepository.save(reserva);
        return convertToDTO(reserva);
    }

    @Override
    @Transactional
    public ReservaDTO update(Long id, ReservaDTO reservaDTO) {
        Optional<Reserva> reservaOptional = reservaRepository.findById(id);
        if (reservaOptional.isPresent()) {
            Reserva reserva = reservaOptional.get();

            LocalDateTime fechaReservaOriginal = reserva.getFechaReserva();
            EstadoReserva estadoOriginal = reserva.getEstado();

            reserva.setDescripcion(reservaDTO.getDescripcion());
            reserva.setFechaInicio(reservaDTO.getFechaInicio());
            reserva.setFechaFin(reservaDTO.getFechaFin());
            reserva.setEstado(reservaDTO.getEstado() != null ? reservaDTO.getEstado() : estadoOriginal);

            if (reservaDTO.getUsuarioId() != null) {
                usuarioRepository.findById(reservaDTO.getUsuarioId())
                        .ifPresent(reserva::setUsuario);
            }

            if (reservaDTO.getVehiculoIds() != null && !reservaDTO.getVehiculoIds().isEmpty()) {
                List<Vehiculo> vehiculos = vehiculoRepository.findAllById(reservaDTO.getVehiculoIds());
                reserva.setVehiculos(vehiculos);
            }

            reserva.setFechaReserva(fechaReservaOriginal);

            reserva = reservaRepository.save(reserva);
            return convertToDTO(reserva);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reservaRepository.deleteById(id);
    }

    @Override
    public boolean isVehiculoAvailable(Long vehiculoId, LocalDateTime startDate, LocalDateTime endDate) {
        return !reservaRepository.existsReservaForVehiculoInPeriod(vehiculoId, startDate, endDate);
    }

    @Override
    public boolean areAllVehiculosAvailable(List<Long> vehiculosIds, LocalDateTime startDate, LocalDateTime endDate) {
        for (Long vehiculoId : vehiculosIds) {
            if (!isVehiculoAvailable(vehiculoId, startDate, endDate)) {
                return false;
            }
        }
        return true;
    }

    private ReservaDTO convertToDTO(Reserva reserva) {
        List<Long> vehiculoIds = reserva.getVehiculos().stream()
                .map(Vehiculo::getId)
                .collect(Collectors.toList());

        return ReservaDTO.builder()
                .id(reserva.getId())
                .descripcion(reserva.getDescripcion())
                .fechaInicio(reserva.getFechaInicio())
                .fechaFin(reserva.getFechaFin())
                .fechaReserva(reserva.getFechaReserva())
                .usuarioId(reserva.getUsuario() != null ? reserva.getUsuario().getId() : null)
                .vehiculoIds(vehiculoIds)
                .estado(reserva.getEstado())
                .build();
    }

    private Reserva convertToEntity(ReservaDTO reservaDTO) {
        Reserva reserva = new Reserva();
        reserva.setId(reservaDTO.getId());
        reserva.setDescripcion(reservaDTO.getDescripcion());
        reserva.setFechaInicio(reservaDTO.getFechaInicio());
        reserva.setFechaFin(reservaDTO.getFechaFin());
        reserva.setFechaReserva(reservaDTO.getFechaReserva());
        reserva.setEstado(reservaDTO.getEstado() != null ? reservaDTO.getEstado() : EstadoReserva.RESERVADO);

        if (reservaDTO.getUsuarioId() != null) {
            usuarioRepository.findById(reservaDTO.getUsuarioId())
                    .ifPresent(reserva::setUsuario);
        }

        if (reservaDTO.getVehiculoIds() != null && !reservaDTO.getVehiculoIds().isEmpty()) {
            List<Vehiculo> vehiculos = vehiculoRepository.findAllById(reservaDTO.getVehiculoIds());
            reserva.setVehiculos(vehiculos);
        } else {
            reserva.setVehiculos(new ArrayList<>());
        }

        return reserva;
    }
}