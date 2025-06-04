package com.reservas.vehiculos.institucionales.mapper;

import com.reservas.vehiculos.institucionales.dto.UsuarioDTO;
import com.reservas.vehiculos.institucionales.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioDTO.UsuarioMostrarPerfil toUsuarioMostrarPerfil(Usuario usuario);

    UsuarioDTO.UsuariosLista toUsuarioLista(Usuario usuario);

    Usuario toEntity(UsuarioDTO.UsuarioCrearDTO usuarioDTO);

    UsuarioDTO.UsuarioCrearDTO toUsuarioCrear(Usuario usuario);
    UsuarioDTO.UsuarioModificarDTO toUsuarioModificar(Usuario usuario);
}

