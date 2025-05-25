package com.reservas.vehiculos.institucionales.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float costo;

    private String descripcion;


    @Column(name = "doc_factura")
    private String docFactura;

    @Column(name = "fecha_reparacion")
    private LocalDateTime fechaReparacion;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
}
