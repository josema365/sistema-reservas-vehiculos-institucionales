package com.reservas.vehiculos.institucionales.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlacaValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPlaca {
    String message() default "La placa debe tener formato AAA999";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
