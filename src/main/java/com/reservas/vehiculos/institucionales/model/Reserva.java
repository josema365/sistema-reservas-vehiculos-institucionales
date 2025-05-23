package com.reservas.vehiculos.institucionales.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private LocalDateTime fecha_inicio;

    private LocalDateTime feche_fin;

    private LocalDateTime fecha_reserva;

    @ManyToMany
    @JoinTable(
            name = "reserva_vehiculo",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "vehiculo_id")
    )
    private List<Vehiculo> vehiculos;



    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
