package cl.martinez.backend_puppy_chop.controllers;

import cl.martinez.backend_puppy_chop.contracts.IFavoriteProductService;
import cl.martinez.backend_puppy_chop.models.FavoriteProduct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteProductController {

    private final IFavoriteProductService favoriteProductService;

    @Autowired
    public FavoriteProductController(IFavoriteProductService favoriteProductService) {
        this.favoriteProductService = favoriteProductService;
    }

    @PostMapping
    public ResponseEntity<?> agregarFavorito(@Valid @RequestBody FavoriteProduct favoriteProduct) {
        try {
            FavoriteProduct nuevoFavorito = favoriteProductService.agregarFavorito(favoriteProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Producto agregado a favoritos exitosamente",
                    "favorito", nuevoFavorito
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al agregar favorito"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerFavorito(@PathVariable Long id) {
        try {
            return favoriteProductService.obtenerFavoritoPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener favorito"));
        }
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<?> listarFavoritosPorUsuario(@PathVariable Long userId) {
        try {
            List<FavoriteProduct> favoritos = favoriteProductService.obtenerFavoritosPorUsuario(userId);
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar favoritos"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long id) {
        try {
            favoriteProductService.eliminarFavorito(id);
            return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado de favoritos exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar favorito"));
        }
    }

    @GetMapping("/usuario/{userId}/verificar/{productId}")
    public ResponseEntity<?> verificarFavorito(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            boolean existe = favoriteProductService.existeFavorito(userId, productId);
            return ResponseEntity.ok(Map.of("esFavorito", existe));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar favorito"));
        }
    }
}