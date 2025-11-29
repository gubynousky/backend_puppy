package cl.martinez.backend_puppy_chop.services.implementations;

import cl.martinez.backend_puppy_chop.contracts.IFavoriteProductService;
import cl.martinez.backend_puppy_chop.models.FavoriteProduct;
import cl.martinez.backend_puppy_chop.repositories.FavoriteProductRepository;
import cl.martinez.backend_puppy_chop.repositories.ProductRepository;
import cl.martinez.backend_puppy_chop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FavoriteProductServiceImpl implements IFavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public FavoriteProductServiceImpl(FavoriteProductRepository favoriteProductRepository,
                                      UserRepository userRepository,
                                      ProductRepository productRepository) {
        this.favoriteProductRepository = favoriteProductRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public FavoriteProduct agregarFavorito(FavoriteProduct favoriteProduct) {
        if (!userRepository.existsById(favoriteProduct.getUser().getId())) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        if (!productRepository.existsById(favoriteProduct.getProduct().getId())) {
            throw new IllegalArgumentException("Producto no encontrado");
        }

        if (existeFavorito(favoriteProduct.getUser().getId(), favoriteProduct.getProduct().getId())) {
            throw new IllegalArgumentException("Este producto ya está en favoritos");
        }

        return favoriteProductRepository.save(favoriteProduct);
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
    public void eliminarFavorito(Long id) {
        if (!favoriteProductRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto favorito no encontrado");
        }
        favoriteProductRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeFavorito(Long userId, Long productId) {
        return favoriteProductRepository.existsByUserIdAndProductId(userId, productId);
    }
}