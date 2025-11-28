package cl.martinez.backend_puppy_chop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos_favoritos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "producto_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavouriteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Product product;

    @NotBlank(message = "La categoria de interes es obligatoria")
    @Column(name = "categoria_interes", nullable = false, length = 50)
    private String categoriaInteres;

    @Column(name = "notificar_ofertas", nullable = false)
    @Builder.Default
    private Boolean notificarOfertas = false;

    @CreationTimestamp
    @Column(name = "fecha_agregado", nullable = false, updatable = false)
    private LocalDateTime fechaAgregado;

    // METODOS Y HELPERS
    public boolean categoriaCoincide(){
        return product != null &&
                categoriaInteres != null &&
                categoriaInteres.equalsIgnoreCase(product.getCategoria());
    }

    public String getNombreProducto() {
        return product != null ? product.getNombre() : "Producto no disponible";
    }
}
