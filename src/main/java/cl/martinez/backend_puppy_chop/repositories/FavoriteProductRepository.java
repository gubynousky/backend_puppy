package cl.martinez.backend_puppy_chop.repositories;

import cl.martinez.backend_puppy_chop.models.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findByUserId(Long userId);
    List<FavoriteProduct> findByProductId(Long productId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}