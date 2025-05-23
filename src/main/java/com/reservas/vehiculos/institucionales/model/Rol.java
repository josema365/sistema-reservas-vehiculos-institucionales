package com.reservas.vehiculos.institucionales.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Almacena el nombre del rol como una cadena en la base de datos
    @Column(length = 20) // Longitud máxima de 20 caracteres para el nombre del rol
    private NombreRol nombre;

    public enum NombreRol { // Enum para definir los nombres de los roles
        ROL_USUARIO,
        ROL_INSPECTOR,
        ROL_ADMIN
    }


}
