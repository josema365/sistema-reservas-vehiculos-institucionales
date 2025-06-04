package com.reservas.vehiculos.institucionales.service.impl;

import com.reservas.vehiculos.institucionales.dto.UsuarioDTO;
import com.reservas.vehiculos.institucionales.mapper.UsuarioMapper;
import com.reservas.vehiculos.institucionales.model.Usuario;
import com.reservas.vehiculos.institucionales.repository.UsuarioRepository;
import com.reservas.vehiculos.institucionales.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Cacheable(value = "usuariosLista")
    public List<UsuarioDTO.UsuariosLista> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toUsuarioLista)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "usuarioPerfil", key = "#id")
    public UsuarioDTO.UsuarioMostrarPerfil obtenerUsuarioDatos(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toUsuarioMostrarPerfil(usuario);
    }

    @Override
    @CacheEvict(value = { "usuarioPerfil", "usuariosLista" }, key = "#id", allEntries = true)
    public void DarBajaUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstadoCuenta(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @CacheEvict(value = "usuariosLista", allEntries = true)
    public UsuarioDTO.UsuarioCrearDTO crearUsuario(UsuarioDTO.UsuarioCrearDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        return usuarioMapper.toUsuarioCrear(usuarioRepository.save(usuario));
    }

    @Override
    @CacheEvict(value = { "usuarioPerfil", "usuariosLista" }, key = "#usuarioDTO.id", allEntries = true)
    public UsuarioDTO.UsuarioModificarDTO modificarUsuario(UsuarioDTO.UsuarioModificarDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidoPaterno(usuarioDTO.getApellidoPaterno());
        usuario.setApellidoMaterno(usuarioDTO.getApellidoMaterno());
        usuario.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setCiudad(usuarioDTO.getCiudad());
        usuario.setGenero(usuarioDTO.getGenero());
        usuario.setUrlImg(usuarioDTO.getUrlImg());
        return usuarioMapper.toUsuarioModificar(usuarioRepository.save(usuario));
    }
}
