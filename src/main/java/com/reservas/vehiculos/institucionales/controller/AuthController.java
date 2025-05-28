package com.reservas.vehiculos.institucionales.controller;

import com.reservas.vehiculos.institucionales.model.Rol;
import com.reservas.vehiculos.institucionales.model.Usuario;
import com.reservas.vehiculos.institucionales.repository.RolRepository;
import com.reservas.vehiculos.institucionales.repository.UsuarioRepository;
import com.reservas.vehiculos.institucionales.security.jwt.JwtUtils;
import com.reservas.vehiculos.institucionales.security.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "APIs para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas o campos requeridos faltantes"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Parameter(description = "Credenciales de usuario", required = true)
            @RequestBody LoginRequest loginRequest) {
        try {
            // Validar que los campos no estén vacíos
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de usuario es requerido");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña es requerida");
            }

            // Intentar autenticar
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", userDetails.getUsername());
            response.put("roles", userDetails.getAuthorities());
            
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Credenciales inválidas. Por favor verifica tu usuario y contraseña.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error de autenticación: " + e.getMessage());
        }
    }

    @Operation(summary = "Registrar administrador", description = "Registra un nuevo usuario con rol de administrador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario/email ya existente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/registrar-admin")
    public ResponseEntity<?> registrarAdmin(
            @Parameter(description = "Datos del administrador", required = true)
            @RequestBody RegistroRequest registroRequest) {
        try {
            // Validar campos requeridos
            if (registroRequest.getUsername() == null || registroRequest.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de usuario es requerido");
            }
            if (registroRequest.getPassword() == null || registroRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña es requerida");
            }
            if (registroRequest.getEmail() == null || registroRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El email es requerido");
            }

            // Verificar si el usuario ya existe
            if (usuarioRepository.existsByUsuario(registroRequest.getUsername())) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
            }

            if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
                return ResponseEntity.badRequest().body("El email ya está en uso");
            }

            // Crear nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setUsuario(registroRequest.getUsername());
            usuario.setEmail(registroRequest.getEmail());
            usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
            usuario.setNombre(registroRequest.getNombre());
            usuario.setApellidoPaterno(registroRequest.getApellidoPaterno());
            usuario.setApellidoMaterno(registroRequest.getApellidoMaterno());
            usuario.setFechaRegistro(LocalDateTime.now());

            // Asignar rol de administrador
            Set<Rol> roles = new HashSet<>();
            Rol rolAdmin = rolRepository.findByNombre(Rol.NombreRol.ROL_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado. Asegúrate de que la base de datos esté inicializada correctamente."));
            roles.add(rolAdmin);
            usuario.setRoles(roles);

            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Usuario administrador registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar el usuario: " + e.getMessage());
        }
    }
}

@Schema(description = "Solicitud de login")
class LoginRequest {
    @Schema(description = "Nombre de usuario", example = "admin", required = true)
    private String username;
    
    @Schema(description = "Contraseña", example = "123456", required = true)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

@Schema(description = "Respuesta de login exitoso")
class LoginResponse {
    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;
    
    @Schema(description = "Roles del usuario", example = "[\"ROLE_ADMIN\"]")
    private Object roles;

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getRoles() {
        return roles;
    }

    public void setRoles(Object roles) {
        this.roles = roles;
    }
}

@Schema(description = "Solicitud de registro de administrador")
class RegistroRequest {
    @Schema(description = "Nombre de usuario", example = "admin", required = true)
    private String username;
    
    @Schema(description = "Correo electrónico", example = "admin@example.com", required = true)
    private String email;
    
    @Schema(description = "Contraseña", example = "123456", required = true)
    private String password;
    
    @Schema(description = "Nombre", example = "Administrador")
    private String nombre;
    
    @Schema(description = "Apellido paterno", example = "Sistema")
    private String apellidoPaterno;
    
    @Schema(description = "Apellido materno", example = "Principal")
    private String apellidoMaterno;

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
} 