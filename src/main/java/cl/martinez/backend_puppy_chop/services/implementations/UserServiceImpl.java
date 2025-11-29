package cl.martinez.backend_puppy_chop.services.implementations;

import cl.martinez.backend_puppy_chop.contracts.IUserService;
import cl.martinez.backend_puppy_chop.models.User;
import cl.martinez.backend_puppy_chop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registrarUsuario(User user) {
        if (existeUsuarioPorEmail(user.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> obtenerUsuarioPorId(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> obtenerUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> obtenerUsuariosActivos() {
        return userRepository.findByActivoTrue();
    }

    @Override
    public User actualizarUsuario(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        existingUser.setNombre(user.getNombre());
        existingUser.setApellido(user.getApellido());
        existingUser.setTelefono(user.getTelefono());
        existingUser.setFechaNacimiento(user.getFechaNacimiento());

        return userRepository.save(existingUser);
    }

    @Override
    public void desactivarUsuario(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setActivo(false);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarCredenciales(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        if (!user.getActivo()) {
            return false;
        }

        return password.equals(user.getPassword());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> buscarUsuariosPorNombre(String nombre) {
        return userRepository.findByNombreContainingIgnoreCase(nombre);
    }
}