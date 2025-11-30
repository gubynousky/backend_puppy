package cl.martinez.backend_puppy_chop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "productos_favoritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    @NotNull
    private Product product;

    @NotBlank
    @Column(name = "categoria_interes", nullable = false)
    private String categoriaInteres;

    @Column(name = "notificar_ofertas", nullable = false)
    @Builder.Default
    private Boolean notificarOfertas = false;

    @Column(name = "fecha_agregado")
    private LocalDateTime fechaAgregado;

    @PrePersist
    public void prePersist() {
        fechaAgregado = LocalDateTime.now();
        if (notificarOfertas == null) notificarOfertas = false;
    }
}