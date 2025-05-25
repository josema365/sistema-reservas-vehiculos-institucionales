package com.reservas.vehiculos.institucionales.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    private BigDecimal costo;

    private String descripcion;


    @Column(name = "doc_factura")
    private String docFactura;

    @Column(name = "fecha_reparacion")

    private LocalDateTime fechaReparacion;

    @ManyToOne
    @JoinColumn(name = "id_auto")
    private Vehiculo vehiculo;
}
