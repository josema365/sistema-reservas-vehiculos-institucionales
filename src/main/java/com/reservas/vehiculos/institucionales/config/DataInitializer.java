package com.reservas.vehiculos.institucionales.config;

import com.reservas.vehiculos.institucionales.model.Rol;
import com.reservas.vehiculos.institucionales.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles si no existen
        for (Rol.NombreRol nombreRol : Rol.NombreRol.values()) {
            if (!rolRepository.findByNombre(nombreRol).isPresent()) {
                Rol rol = new Rol();
                rol.setNombre(nombreRol);
                rolRepository.save(rol);
            }
        }
    }
} 