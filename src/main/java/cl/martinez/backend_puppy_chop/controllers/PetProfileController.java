package cl.martinez.backend_puppy_chop.controllers;

import cl.martinez.backend_puppy_chop.contracts.IPetProfileService;
import cl.martinez.backend_puppy_chop.models.PetProfile;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            PetProfile nuevoPerfil = petProfileService.crearPerfilMascota(petProfile, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Perfil de mascota creado exitosamente",
                    "mascota", nuevoPerfil
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear perfil de mascota"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPerfilMascota(@PathVariable Long id) {
        try {
            return petProfileService.obtenerPerfilPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener perfil de mascota"));
        }
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<?> listarMascotasPorUsuario(@PathVariable Long userId) {
        try {
            List<PetProfile> mascotas = petProfileService.obtenerPerfilesPorUsuario(userId);
            return ResponseEntity.ok(mascotas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar mascotas"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfilMascota(@PathVariable Long id, @Valid @RequestBody PetProfile petProfile) {
        try {
            PetProfile perfilActualizado = petProfileService.actualizarPerfil(id, petProfile);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Perfil de mascota actualizado exitosamente",
                    "mascota", perfilActualizado
            ));
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