package cl.martinez.backend_puppy_chop.services.implementations;

import cl.martinez.backend_puppy_chop.contracts.IPetProfileService;
import cl.martinez.backend_puppy_chop.models.PetProfile;
import cl.martinez.backend_puppy_chop.models.User;
import cl.martinez.backend_puppy_chop.repositories.PetProfileRepository;
import cl.martinez.backend_puppy_chop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetProfileServiceImpl implements IPetProfileService {

    private final PetProfileRepository petProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetProfileServiceImpl(PetProfileRepository petProfileRepository, UserRepository userRepository) {
        this.petProfileRepository = petProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PetProfile crearPerfilMascota(PetProfile petProfile, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        petProfile.setUser(user);
        return petProfileRepository.save(petProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetProfile> obtenerPerfilPorId(Long id) {
        return petProfileRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetProfile> obtenerPerfilesPorUsuario(Long userId) {
        return petProfileRepository.findByUserId(userId);
    }

    @Override
    public PetProfile actualizarPerfil(Long id, PetProfile petProfile) {
        PetProfile existingProfile = petProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de mascota no encontrado"));

        existingProfile.setNombreMascota(petProfile.getNombreMascota());
        existingProfile.setTipo(petProfile.getTipo());
        existingProfile.setRaza(petProfile.getRaza());
        existingProfile.setEdad(petProfile.getEdad());
        existingProfile.setPeso(petProfile.getPeso());
        existingProfile.setTamaño(petProfile.getTamaño());

        return petProfileRepository.save(existingProfile);
    }

    @Override
    public void eliminarPerfil(Long id) {
        if (!petProfileRepository.existsById(id)) {
            throw new IllegalArgumentException("Perfil de mascota no encontrado");
        }
        petProfileRepository.deleteById(id);
    }
}