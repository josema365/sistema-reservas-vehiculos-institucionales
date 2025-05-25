package com.reservas.vehiculos.institucionales.dto;



import com.reservas.vehiculos.institucionales.validation.ValidPlaca;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehiculoDTO {

    @NotBlank(message = "La placa es obligatoria")
    @ValidPlaca
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;
}


