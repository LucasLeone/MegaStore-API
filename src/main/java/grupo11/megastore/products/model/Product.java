package grupo11.megastore.products.model;

import grupo11.megastore.constant.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 32, message = "El nombre debe tener entre 2 y 32 caracteres")
    private String name;

    @Column(nullable = true)
    @Size(max = 128, message = "La descripción no puede exceder 128 caracteres")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private Double price;

    // @Min(value = 0, message = "El stock no puede ser negativo")
    // private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    // @Lob
    // @Basic(fetch = FetchType.LAZY)
    // @Column(name = "image", nullable = true)
    // private byte[] image;

    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    public void delete() {
        this.setStatus(EntityStatus.DELETED);
    }
}
