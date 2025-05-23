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

    private String doc_factura;

    private LocalDateTime fecha_reparacion;



    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;




}
