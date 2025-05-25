package com.reservas.vehiculos.institucionales.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlacaValidator implements ConstraintValidator<ValidPlaca, String> {

    private static final String PLACA_PATTERN = "^[A-Z]{3}[0-9]{3}$";

    @Override
    public boolean isValid(String placa, ConstraintValidatorContext context) {
        if (placa == null) return false;
        return placa.matches(PLACA_PATTERN);
    }
}

