package grupo11.megastore.products.model;

import grupo11.megastore.constant.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "variants")
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "El color es obligatorio")
    private String color;

    @NotNull(message = "El talle es obligatorio")
    private String size;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image", nullable = true)
    private byte[] image;

    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    public void delete() {
        this.setStatus(EntityStatus.DELETED);
    }
}
