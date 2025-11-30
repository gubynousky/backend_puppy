package cl.martinez.backend_puppy_chop.services.implementations;

import cl.martinez.backend_puppy_chop.contracts.IFavoriteProductService;
import cl.martinez.backend_puppy_chop.models.FavoriteProduct;
import cl.martinez.backend_puppy_chop.models.Product;
import cl.martinez.backend_puppy_chop.models.User;
import cl.martinez.backend_puppy_chop.repositories.FavoriteProductRepository;
import cl.martinez.backend_puppy_chop.repositories.ProductRepository;
import cl.martinez.backend_puppy_chop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteProductServiceImpl implements IFavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public FavoriteProductServiceImpl(
            FavoriteProductRepository favoriteProductRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {
        this.favoriteProductRepository = favoriteProductRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public FavoriteProduct agregarFavorito(Long userId, Long productId, String categoriaInteres, Boolean notificarOfertas) {
        System.out.println("Service - Agregando favorito:");
        System.out.println("  User ID: " + userId);
        System.out.println("  Product ID: " + productId);

        // Verificar que el usuario existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + userId));

        // Verificar que el producto existe
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productId));

        // Verificar si ya existe el favorito
        if (favoriteProductRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new IllegalArgumentException("Este producto ya está en favoritos");
        }

        FavoriteProduct favorito = FavoriteProduct.builder()
                .user(user)
                .product(product)
                .categoriaInteres(categoriaInteres)
                .notificarOfertas(notificarOfertas != null ? notificarOfertas : false)
                .build();

        FavoriteProduct saved = favoriteProductRepository.save(favorito);
        System.out.println("Favorito guardado con ID: " + saved.getId());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriteProduct> obtenerFavoritoPorId(Long id) {
        return favoriteProductRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteProduct> obtenerFavoritosPorUsuario(Long userId) {
        return favoriteProductRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void eliminarFavorito(Long id) {
        if (!favoriteProductRepository.existsById(id)) {
            throw new IllegalArgumentException("Favorito no encontrado");
        }
        favoriteProductRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeFavorito(Long userId, Long productId) {
        return favoriteProductRepository.existsByUserIdAndProductId(userId, productId);
    }
}