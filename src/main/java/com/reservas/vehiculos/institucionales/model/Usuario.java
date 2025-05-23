package com.reservas.vehiculos.institucionales.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.aop.target.LazyInitTargetSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido_paterno;

    private String apellido_materno;

    private LocalDate fecha_nacimiento;

    private String email;

    private String cargo_publico;

    private String usuario;

    private String password;

    private LocalDateTime fecha_registro;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;


    @ManyToMany(fetch = FetchType.EAGER) // Carga los roles de forma anticipada (EAGER) para evitar problemas de LazyInitializationException
    @JoinTable(
            name = "usuario_roles", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"), // Columna que referencia al usuario
            inverseJoinColumns = @JoinColumn(name = "rol_id") // Columna que referencia al rol
    )
    private Set<Rol> roles = new HashSet<>();
}
