package cl.martinez.backend_puppy_chop.repositories;

import cl.martinez.backend_puppy_chop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByActivoTrue();
    List<User> findByNombreContainingIgnoreCase(String nombre);
}