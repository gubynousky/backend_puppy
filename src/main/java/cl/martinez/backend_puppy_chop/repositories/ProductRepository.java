package cl.martinez.backend_puppy_chop.repositories;

import cl.martinez.backend_puppy_chop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoria(String categoria);
    List<Product> findByActivoTrue();
    List<Product> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombre(String nombre);
}