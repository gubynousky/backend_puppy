package cl.martinez.backend_puppy_chop.contracts;

import cl.martinez.backend_puppy_chop.models.PetProfile;
import java.util.List;
import java.util.Optional;

public interface IPetProfileService {
    PetProfile crearPerfilMascota(PetProfile petProfile, Long userId);
    Optional<PetProfile> obtenerPerfilPorId(Long id);
    List<PetProfile> obtenerPerfilesPorUsuario(Long userId);
    PetProfile actualizarPerfil(Long id, PetProfile petProfile);
    void eliminarPerfil(Long id);
}