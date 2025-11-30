package cl.martinez.backend_puppy_chop.contracts;

import cl.martinez.backend_puppy_chop.models.FavoriteProduct;

import java.util.List;
import java.util.Optional;

public interface IFavoriteProductService {
    // NUEVO: Método que acepta IDs individuales
    FavoriteProduct agregarFavorito(Long userId, Long productId, String categoriaInteres, Boolean notificarOfertas);

    Optional<FavoriteProduct> obtenerFavoritoPorId(Long id);
    List<FavoriteProduct> obtenerFavoritosPorUsuario(Long userId);
    void eliminarFavorito(Long id);
    boolean existeFavorito(Long userId, Long productId);
}