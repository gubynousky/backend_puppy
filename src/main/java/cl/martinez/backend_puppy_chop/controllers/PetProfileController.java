package cl.martinez.backend_puppy_chop.controllers;

import cl.martinez.backend_puppy_chop.contracts.IPetProfileService;
import cl.martinez.backend_puppy_chop.models.PetProfile;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/mascotas")
@CrossOrigin(origins = "http://localhost:3000")
public class PetProfileController {

    private final IPetProfileService petProfileService;

    @Autowired
    public PetProfileController(IPetProfileService petProfileService) {
        this.petProfileService = petProfileService;
    }

    @PostMapping("/usuario/{userId}")
    public ResponseEntity<?> crearPerfilMascota(@PathVariable Long userId, @Valid @RequestBody PetProfile petProfile) {
        try {
            System.out.println("=== CREANDO PERFIL DE MASCOTA ===");
            System.out.println("Usuario ID: " + userId);
            System.out.println("Nombre Mascota: " + petProfile.getNombreMascota());
            System.out.println("Tipo: " + petProfile.getTipo());
            System.out.println("Raza: " + petProfile.getRaza());
            System.out.println("Edad: " + petProfile.getEdad());
            System.out.println("Peso: " + petProfile.getPeso());
            System.out.println("Tamaño: " + petProfile.getTamaño());

            PetProfile nuevoPerfil = petProfileService.crearPerfilMascota(petProfile, userId);

            System.out.println("Perfil de mascota creado exitosamente con ID: " + nuevoPerfil.getId());

            // IMPORTANTE: Crear respuesta manual SIN incluir el objeto User completo
            Map<String, Object> mascotaResponse = new HashMap<>();
            mascotaResponse.put("id", nuevoPerfil.getId());
            mascotaResponse.put("nombreMascota", nuevoPerfil.getNombreMascota());
            mascotaResponse.put("tipo", nuevoPerfil.getTipo());
            mascotaResponse.put("raza", nuevoPerfil.getRaza());
            mascotaResponse.put("edad", nuevoPerfil.getEdad());
            mascotaResponse.put("peso", nuevoPerfil.getPeso());
            mascotaResponse.put("tamaño", nuevoPerfil.getTamaño());

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Perfil de mascota creado exitosamente");
            response.put("mascota", mascotaResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación en mascota: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado al crear mascota:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear perfil de mascota: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPerfilMascota(@PathVariable Long id) {
        try {
            Optional<PetProfile> perfilOpt = petProfileService.obtenerPerfilPorId(id);

            if (perfilOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            PetProfile perfil = perfilOpt.get();

            // Crear respuesta manual
            Map<String, Object> mascotaResponse = new HashMap<>();
            mascotaResponse.put("id", perfil.getId());
            mascotaResponse.put("nombreMascota", perfil.getNombreMascota());
            mascotaResponse.put("tipo", perfil.getTipo());
            mascotaResponse.put("raza", perfil.getRaza());
            mascotaResponse.put("edad", perfil.getEdad());
            mascotaResponse.put("peso", perfil.getPeso());
            mascotaResponse.put("tamaño", perfil.getTamaño());

            return ResponseEntity.ok(mascotaResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener perfil de mascota"));
        }
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<?> listarMascotasPorUsuario(@PathVariable Long userId) {
        try {
            List<PetProfile> mascotas = petProfileService.obtenerPerfilesPorUsuario(userId);

            // Crear lista de respuestas manuales
            List<Map<String, Object>> mascotasResponse = mascotas.stream()
                    .map(perfil -> {
                        Map<String, Object> mascotaMap = new HashMap<>();
                        mascotaMap.put("id", perfil.getId());
                        mascotaMap.put("nombreMascota", perfil.getNombreMascota());
                        mascotaMap.put("tipo", perfil.getTipo());
                        mascotaMap.put("raza", perfil.getRaza());
                        mascotaMap.put("edad", perfil.getEdad());
                        mascotaMap.put("peso", perfil.getPeso());
                        mascotaMap.put("tamaño", perfil.getTamaño());
                        return mascotaMap;
                    })
                    .toList();

            return ResponseEntity.ok(mascotasResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar mascotas"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfilMascota(@PathVariable Long id, @Valid @RequestBody PetProfile petProfile) {
        try {
            PetProfile perfilActualizado = petProfileService.actualizarPerfil(id, petProfile);

            // Crear respuesta manual
            Map<String, Object> mascotaResponse = new HashMap<>();
            mascotaResponse.put("id", perfilActualizado.getId());
            mascotaResponse.put("nombreMascota", perfilActualizado.getNombreMascota());
            mascotaResponse.put("tipo", perfilActualizado.getTipo());
            mascotaResponse.put("raza", perfilActualizado.getRaza());
            mascotaResponse.put("edad", perfilActualizado.getEdad());
            mascotaResponse.put("peso", perfilActualizado.getPeso());
            mascotaResponse.put("tamaño", perfilActualizado.getTamaño());

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Perfil de mascota actualizado exitosamente");
            response.put("mascota", mascotaResponse);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar perfil"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPerfilMascota(@PathVariable Long id) {
        try {
            petProfileService.eliminarPerfil(id);
            return ResponseEntity.ok(Map.of("mensaje", "Perfil de mascota eliminado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar perfil"));
        }
    }
}