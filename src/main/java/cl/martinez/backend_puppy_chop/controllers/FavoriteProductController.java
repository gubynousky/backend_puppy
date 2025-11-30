package cl.martinez.backend_puppy_chop.controllers;

import cl.martinez.backend_puppy_chop.contracts.IFavoriteProductService;
import cl.martinez.backend_puppy_chop.models.FavoriteProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<?> agregarFavorito(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("\n========================================");
            System.out.println("=== AGREGANDO FAVORITO ===");
            System.out.println("========================================");
            System.out.println("Payload COMPLETO recibido:");
            payload.forEach((key, value) -> {
                System.out.println("  " + key + " = " + value + " (" + (value != null ? value.getClass().getSimpleName() : "null") + ")");
            });
            System.out.println("========================================\n");

            // Validaciones
            if (payload.get("userId") == null) {
                System.err.println("ERROR: userId es null");
                return ResponseEntity.badRequest().body(Map.of("error", "userId es requerido"));
            }
            if (payload.get("productId") == null) {
                System.err.println("ERROR: productId es null");
                return ResponseEntity.badRequest().body(Map.of("error", "productId es requerido"));
            }

            // Conversión segura de IDs
            Long userId = convertirALong(payload.get("userId"), "userId");
            Long productId = convertirALong(payload.get("productId"), "productId");
            String categoriaInteres = payload.get("categoriaInteres") != null ? payload.get("categoriaInteres").toString() : null;
            Boolean notificarOfertas = payload.get("notificarOfertas") != null ? (Boolean) payload.get("notificarOfertas") : false;

            System.out.println("Datos CONVERTIDOS:");
            System.out.println("  userId: " + userId + " (Long)");
            System.out.println("  productId: " + productId + " (Long)");
            System.out.println("  categoriaInteres: " + categoriaInteres);
            System.out.println("  notificarOfertas: " + notificarOfertas);
            System.out.println();

            FavoriteProduct nuevoFavorito = favoriteProductService.agregarFavorito(
                    userId,
                    productId,
                    categoriaInteres,
                    notificarOfertas
            );

            Map<String, Object> favoritoResponse = new HashMap<>();
            favoritoResponse.put("id", nuevoFavorito.getId());
            favoritoResponse.put("categoriaInteres", nuevoFavorito.getCategoriaInteres());
            favoritoResponse.put("notificarOfertas", nuevoFavorito.getNotificarOfertas());
            favoritoResponse.put("fechaAgregado", nuevoFavorito.getFechaAgregado());

            System.out.println("✅ FAVORITO CREADO EXITOSAMENTE - ID: " + nuevoFavorito.getId());
            System.out.println("========================================\n");

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Favorito agregado exitosamente",
                    "favorito", favoritoResponse
            ));
        } catch (Exception e) {
            System.err.println("\n❌ ERROR AL AGREGAR FAVORITO:");
            System.err.println("Tipo de error: " + e.getClass().getSimpleName());
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            System.err.println("========================================\n");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al agregar favorito: " + e.getMessage()));
        }
    }

    // Método auxiliar para convertir a Long de forma segura
    private Long convertirALong(Object valor, String nombreCampo) {
        try {
            if (valor instanceof Integer) {
                return ((Integer) valor).longValue();
            } else if (valor instanceof Long) {
                return (Long) valor;
            } else if (valor instanceof String) {
                return Long.parseLong((String) valor);
            } else {
                return Long.parseLong(valor.toString());
            }
        } catch (Exception e) {
            System.err.println("ERROR al convertir " + nombreCampo + ": " + e.getMessage());
            throw new IllegalArgumentException("Error al convertir " + nombreCampo + " a Long");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerFavorito(@PathVariable Long id) {
        try {
            return favoriteProductService.obtenerFavoritoPorId(id)
                    .map(fav -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("id", fav.getId());
                        response.put("categoriaInteres", fav.getCategoriaInteres());
                        response.put("notificarOfertas", fav.getNotificarOfertas());
                        response.put("fechaAgregado", fav.getFechaAgregado());
                        return ResponseEntity.ok(response);
                    })
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

            List<Map<String, Object>> favoritosResponse = favoritos.stream()
                    .map(fav -> {
                        Map<String, Object> favMap = new HashMap<>();
                        favMap.put("id", fav.getId());
                        favMap.put("categoriaInteres", fav.getCategoriaInteres());
                        favMap.put("notificarOfertas", fav.getNotificarOfertas());
                        favMap.put("fechaAgregado", fav.getFechaAgregado());

                        if (fav.getProduct() != null) {
                            Map<String, Object> productoMap = new HashMap<>();
                            productoMap.put("id", fav.getProduct().getId());
                            productoMap.put("nombre", fav.getProduct().getNombre());
                            productoMap.put("descripcion", fav.getProduct().getDescripcion());
                            productoMap.put("precio", fav.getProduct().getPrecio());
                            productoMap.put("categoria", fav.getProduct().getCategoria());
                            favMap.put("producto", productoMap);
                        }

                        return favMap;
                    })
                    .toList();

            return ResponseEntity.ok(favoritosResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar favoritos"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long id) {
        try {
            favoriteProductService.eliminarFavorito(id);
            return ResponseEntity.ok(Map.of("mensaje", "Favorito eliminado exitosamente"));
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
            return ResponseEntity.ok(Map.of("existe", existe));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar favorito"));
        }
    }
}