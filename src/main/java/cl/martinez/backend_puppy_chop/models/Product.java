package cl.martinez.backend_puppy_chop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Email
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracterea")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 500, message = "La descripcion no debe exceder 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotBlank(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private Double precio;

    @NotBlank(message = "La categoria es obligatoria")
    @Pattern(regexp = "alimento|snacks|juguetes|salud|higiene|accesorios",
            message = "Categoría inválida. Debe ser: alimento, snacks, juguetes, salud, higiene o accesorios")
    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(length = 255)
    private String imagen;

    @NotBlank(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // RELACIONES

}
