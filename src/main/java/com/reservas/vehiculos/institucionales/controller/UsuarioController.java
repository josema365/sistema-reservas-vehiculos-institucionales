package com.reservas.vehiculos.institucionales.controller;


import com.reservas.vehiculos.institucionales.dto.UsuarioDTO;
import com.reservas.vehiculos.institucionales.model.Usuario;
import com.reservas.vehiculos.institucionales.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO.UsuariosLista>> obtenerTodosLosUsuarios(){
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<UsuarioDTO.UsuarioMostrarPerfil> obtenerInformacionPersonalDeUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioDatos(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO.UsuarioCrearDTO> crearUsuario(@RequestBody UsuarioDTO.UsuarioCrearDTO usuarioDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuarioDTO));
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO.UsuarioModificarDTO> modificarUsuario(@RequestBody UsuarioDTO.UsuarioModificarDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.modificarUsuario(usuarioDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> darBajaUsuario(@PathVariable Long id){
        usuarioService.DarBajaUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
