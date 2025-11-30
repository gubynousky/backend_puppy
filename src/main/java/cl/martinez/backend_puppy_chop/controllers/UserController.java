package cl.martinez.backend_puppy_chop.controllers;

import cl.martinez.backend_puppy_chop.contracts.IUserService;
import cl.martinez.backend_puppy_chop.models.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody User user) {
        try {
            System.out.println("=== REGISTRO DE USUARIO ===");
            System.out.println("Email: " + user.getEmail());

            User nuevoUsuario = userService.registrarUsuario(user);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("usuario", nuevoUsuario);

            System.out.println("✓ Usuario registrado con ID: " + nuevoUsuario.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar usuario: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            System.out.println("Intento de login para: " + email);

            if (userService.validarCredenciales(email, password)) {
                User user = userService.obtenerUsuarioPorEmail(email).orElseThrow();

                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Login exitoso");
                response.put("usuario", user);

                System.out.println("✓ Login exitoso");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al iniciar sesión"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        try {
            return userService.obtenerUsuarioPorId(id)
                    .map(user -> {
                        Map<String, Object> usuarioResponse = crearUsuarioConMascotasYFavoritos(user);
                        return ResponseEntity.ok(usuarioResponse);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener usuario"));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        try {
            List<User> usuarios = userService.obtenerUsuariosActivos();

            List<Map<String, Object>> usuariosResponse = usuarios.stream()
                    .map(this::crearUsuarioConMascotasYFavoritos)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(usuariosResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar usuarios"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            User usuarioActualizado = userService.actualizarUsuario(id, user);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Usuario actualizado exitosamente",
                    "usuario", usuarioActualizado
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar usuario"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long id) {
        try {
            userService.desactivarUsuario(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario desactivado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al desactivar usuario"));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<User> usuarios = userService.buscarUsuariosPorNombre(nombre);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar usuarios"));
        }
    }

    // MÉTODO AUXILIAR - ORDEN CORRECTO: id, nombre, apellido, email, telefono, fecha, mascotas, favoritos
    private Map<String, Object> crearUsuarioConMascotasYFavoritos(User user) {
        Map<String, Object> usuarioResponse = new LinkedHashMap<>();

        // DATOS DEL USUARIO - EN ORDEN
        usuarioResponse.put("id", user.getId());
        usuarioResponse.put("nombre", user.getNombre());
        usuarioResponse.put("apellido", user.getApellido());
        usuarioResponse.put("email", user.getEmail());
        usuarioResponse.put("password", user.getPassword());
        usuarioResponse.put("telefono", user.getTelefono());
        usuarioResponse.put("fechaNacimiento", user.getFechaNacimiento());
        usuarioResponse.put("fechaRegistro", user.getFechaRegistro());
        usuarioResponse.put("activo", user.getActivo());

        // MASCOTAS
        if (user.getMascotas() != null && !user.getMascotas().isEmpty()) {
            List<Map<String, Object>> mascotasResponse = user.getMascotas().stream()
                    .map(mascota -> {
                        Map<String, Object> mascotaMap = new LinkedHashMap<>();
                        mascotaMap.put("id", mascota.getId());
                        mascotaMap.put("nombreMascota", mascota.getNombreMascota());
                        mascotaMap.put("tipo", mascota.getTipo());
                        mascotaMap.put("raza", mascota.getRaza());
                        mascotaMap.put("edad", mascota.getEdad());
                        mascotaMap.put("peso", mascota.getPeso());
                        mascotaMap.put("tamaño", mascota.getTamaño());
                        return mascotaMap;
                    })
                    .collect(Collectors.toList());
            usuarioResponse.put("mascotas", mascotasResponse);
        } else {
            usuarioResponse.put("mascotas", new ArrayList<>());
        }

        // PRODUCTOS FAVORITOS
        if (user.getProductosFavoritos() != null && !user.getProductosFavoritos().isEmpty()) {
            List<Map<String, Object>> favoritosResponse = user.getProductosFavoritos().stream()
                    .map(favorito -> {
                        Map<String, Object> favoritoMap = new LinkedHashMap<>();
                        favoritoMap.put("id", favorito.getId());
                        favoritoMap.put("categoriaInteres", favorito.getCategoriaInteres());
                        favoritoMap.put("notificarOfertas", favorito.getNotificarOfertas());
                        favoritoMap.put("fechaAgregado", favorito.getFechaAgregado());

                        if (favorito.getProduct() != null) {
                            Map<String, Object> productoMap = new LinkedHashMap<>();
                            productoMap.put("id", favorito.getProduct().getId());
                            productoMap.put("nombre", favorito.getProduct().getNombre());
                            productoMap.put("descripcion", favorito.getProduct().getDescripcion());
                            productoMap.put("precio", favorito.getProduct().getPrecio());
                            productoMap.put("categoria", favorito.getProduct().getCategoria());
                            favoritoMap.put("producto", productoMap);
                        }

                        return favoritoMap;
                    })
                    .collect(Collectors.toList());
            usuarioResponse.put("productosFavoritos", favoritosResponse);
        } else {
            usuarioResponse.put("productosFavoritos", new ArrayList<>());
        }

        return usuarioResponse;
    }
}