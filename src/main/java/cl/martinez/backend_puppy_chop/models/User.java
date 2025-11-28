package cl.martinez.backend_puppy_chop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "el nombre debe contener entre 2 y 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "el apellido es obligatorio")
    @Size(min = 2, max = 50, message = "el apellido debe contener entre 2 y 50 caracteres")
    @Column(nullable = false, length = 50)
    private String apellido;

    @NotBlank(message = "el correo es obligatorio")
    @Email(message = "el email debe der valido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "la contrseña es obligatoria")
    @Size(min = 6, max = 12, message = "la contraseña debe tener un minimo de 6 caracteres y un maximo de 12 caracteres")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "el telefono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9\\s-]{9,15}$", message = "Teléfono inválido")
    @Column(nullable = false, length = 20)
    private String telefono;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de naciemito debe ser en el pasado")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // RELACIONES

}
