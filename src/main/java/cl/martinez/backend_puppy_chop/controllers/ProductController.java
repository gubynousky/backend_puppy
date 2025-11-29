package cl.martinez.backend_puppy_chop.controllers;

import cl.martinez.backend_puppy_chop.contracts.IProductService;
import cl.martinez.backend_puppy_chop.models.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> listarProductos() {
        try {
            List<Product> productos = productService.obtenerProductosActivos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al listar productos"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {
        try {
            return productService.obtenerProductoPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener producto"));
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> obtenerPorCategoria(@PathVariable String categoria) {
        try {
            List<Product> productos = productService.obtenerProductosPorCategoria(categoria);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al filtrar productos"));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<Product> productos = productService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al buscar productos"));
        }
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Product product) {
        try {
            Product nuevoProducto = productService.crearProducto(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Producto creado exitosamente",
                    "producto", nuevoProducto
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear producto"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            Product productoActualizado = productService.actualizarProducto(id, product);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Producto actualizado exitosamente",
                    "producto", productoActualizado
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar producto"));
        }
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        try {
            Integer nuevoStock = body.get("stock");
            productService.actualizarStock(id, nuevoStock);
            return ResponseEntity.ok(Map.of("mensaje", "Stock actualizado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar stock"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarProducto(@PathVariable Long id) {
        try {
            productService.desactivarProducto(id);
            return ResponseEntity.ok(Map.of("mensaje", "Producto desactivado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al desactivar producto"));
        }
    }
}