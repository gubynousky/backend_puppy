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

    @NotBlank
    @Column(name = "nombre_mascota", nullable = false)
    private String nombreMascota;

    @NotBlank
    @Column(nullable = false)
    private String tipo;

    private String raza;

    @NotNull
    @Column(nullable = false)
    private Integer edad;

    @NotNull
    @Column(nullable = false)
    private Double peso;

    @NotBlank
    @Column(nullable = false)
    private String tamaño;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;
}