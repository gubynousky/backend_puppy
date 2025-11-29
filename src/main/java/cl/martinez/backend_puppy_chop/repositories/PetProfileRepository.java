package cl.martinez.backend_puppy_chop.repositories;

import cl.martinez.backend_puppy_chop.models.PetProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetProfileRepository extends JpaRepository<PetProfile, Long> {
    List<PetProfile> findByUserId(Long userId);
    List<PetProfile> findByTipo(String tipo);
}