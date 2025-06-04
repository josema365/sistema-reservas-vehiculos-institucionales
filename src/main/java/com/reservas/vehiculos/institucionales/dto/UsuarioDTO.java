package com.reservas.vehiculos.institucionales.dto;

import com.reservas.vehiculos.institucionales.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class UsuarioDTO {


    @Data
    public static class UsuarioMostrarPerfil{
        private Long id;

        private String nombre;

        private String apellidoPaterno;

        private String apellidoMaterno;

        private LocalDate fechaNacimiento;

        private String email;

        private String cargoInstitucional;

        private LocalDateTime fechaRegistro;

        private String usuario;

        private LocalDateTime fechaUltimaModificacion;

        private String ciudad;

        private String genero;

        private String urlImg;

    }
    @Data
    public static class UsuariosLista{
        private Long id;

        private String nombre;

        private String apellidoPaterno;

        private String apellidoMaterno;

        private String cargoInstitucional;

        private String usuario;

        private String ciudad;
    }
    @Data
    public static class UsuarioCrearDTO {
        @NotBlank
        private String nombre;

        @NotBlank
        private String apellidoPaterno;

        private String apellidoMaterno;

        @NotNull
        private LocalDate fechaNacimiento;

        @NotBlank
        @Email
        private String email;

        private String cargoInstitucional;

        @NotBlank
        private String usuario;

        @NotBlank
        private String password;

        private String ciudad;

        private String genero;

        private String urlImg;



    }

    @Data
    public static class UsuarioModificarDTO {
        @NotNull
        private Long id;

        private String nombre;

        private String apellidoPaterno;

        private String apellidoMaterno;

        private LocalDate fechaNacimiento;

        private String email;

        private String ciudad;

        private String genero;

        private String urlImg;

    }



}
