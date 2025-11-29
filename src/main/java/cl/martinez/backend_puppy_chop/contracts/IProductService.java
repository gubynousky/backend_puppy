package cl.martinez.backend_puppy_chop.contracts;

import cl.martinez.backend_puppy_chop.models.Product;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product crearProducto(Product product);
    Optional<Product> obtenerProductoPorId(Long id);
    List<Product> obtenerProductosActivos();
    List<Product> obtenerProductosPorCategoria(String categoria);
    Product actualizarProducto(Long id, Product product);
    void actualizarStock(Long id, Integer nuevoStock);
    void desactivarProducto(Long id);
    List<Product> buscarProductosPorNombre(String nombre);
}