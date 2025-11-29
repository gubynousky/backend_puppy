package cl.martinez.backend_puppy_chop.services.implementations;

import cl.martinez.backend_puppy_chop.contracts.IProductService;
import cl.martinez.backend_puppy_chop.models.Product;
import cl.martinez.backend_puppy_chop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product crearProducto(Product product) {
        if (productRepository.existsByNombre(product.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> obtenerProductoPorId(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> obtenerProductosActivos() {
        return productRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> obtenerProductosPorCategoria(String categoria) {
        return productRepository.findByCategoria(categoria);
    }

    @Override
    public Product actualizarProducto(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        existingProduct.setNombre(product.getNombre());
        existingProduct.setDescripcion(product.getDescripcion());
        existingProduct.setPrecio(product.getPrecio());
        existingProduct.setCategoria(product.getCategoria());
        existingProduct.setImagen(product.getImagen());

        return productRepository.save(existingProduct);
    }

    @Override
    public void actualizarStock(Long id, Integer nuevoStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.setStock(nuevoStock);
        productRepository.save(product);
    }

    @Override
    public void desactivarProducto(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.setActivo(false);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> buscarProductosPorNombre(String nombre) {
        return productRepository.findByNombreContainingIgnoreCase(nombre);
    }
}