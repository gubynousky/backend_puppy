package cl.martinez.backend_puppy_chop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perfiles_mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "nombre_mascota", nullable = false, length = 50)
    private String nombreMascota;

    @NotBlank(message = "El tipo de mascota e obligatorio")
    @Pattern(regexp = "perro|gato|otro", message = "Tipo inválido. Debe ser: perro, gato u otro")
    @Column(nullable = false, length = 20)
    private String tipo;

    @Size(max = 50, message = "La raza no puede exeder 50 caracteres")
    @Column(length = 50)
    private String raza;

    @NotBlank(message = "L edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 30, message = "La edad no puede ser mayor a 30 años")
    @Column(nullable = false)
    private Integer edad;

    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @Column(nullable = false)
    private Double peso;

    @NotBlank(message = "El ta,año es obligatorio")
    @Pattern(regexp = "pequeño|mediano|grande", message = "Tamaño inválido. Debe ser: pequeño, mediano o grande")
    @Column(nullable = false, length = 20)
    private String tamanio;

    // RELACIONES
}
