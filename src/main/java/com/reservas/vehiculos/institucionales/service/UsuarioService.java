package com.reservas.vehiculos.institucionales.service;


import com.reservas.vehiculos.institucionales.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO.UsuariosLista> obtenerTodosLosUsuarios();
    UsuarioDTO.UsuarioMostrarPerfil obtenerUsuarioDatos(Long id);
    void DarBajaUsuario(Long id);
    UsuarioDTO.UsuarioCrearDTO crearUsuario(UsuarioDTO.UsuarioCrearDTO usuarioDTO);
    UsuarioDTO.UsuarioModificarDTO modificarUsuario(UsuarioDTO.UsuarioModificarDTO usuarioDTO);
}
