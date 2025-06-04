package com.reservas.vehiculos.institucionales.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "La placa es obligatoria")
    @Column(unique = true, length = 10)
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @JsonManagedReference
    @OneToMany(mappedBy = "vehiculo")
    private List<Reparacion> reparaciones;

    @ManyToMany(mappedBy = "vehiculos")
    @JsonManagedReference
    private List<Reserva> reservas;
}
