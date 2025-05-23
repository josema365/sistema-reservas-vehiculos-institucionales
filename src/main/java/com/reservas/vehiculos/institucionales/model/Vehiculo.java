package com.reservas.vehiculos.institucionales.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;

    private String marca;

    private String tipo;

    @OneToMany(mappedBy = "vehiculo")
    private List<Reparacion> reparaciones;


    @ManyToMany(mappedBy = "vehiculos")
    private List<Reserva> reservas;
}
