package cl.martinez.backend_puppy_chop.contracts;

import cl.martinez.backend_puppy_chop.models.User;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    User registrarUsuario(User user);
    Optional<User> obtenerUsuarioPorId(Long id);
    Optional<User> obtenerUsuarioPorEmail(String email);
    List<User> obtenerUsuariosActivos();
    User actualizarUsuario(Long id, User user);
    void desactivarUsuario(Long id);
    boolean existeUsuarioPorEmail(String email);
    boolean validarCredenciales(String email, String password);
    List<User> buscarUsuariosPorNombre(String nombre);
}