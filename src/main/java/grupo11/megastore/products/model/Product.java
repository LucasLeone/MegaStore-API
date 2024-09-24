// package grupo11.megastore.products.model;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.*;
// import lombok.Data;
// import lombok.ToString;

// @Entity
// @Data
// @ToString
// public class Product {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer id;

//     @NotBlank(message = "El nombre del producto es obligatorio")
//     @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
//     private String name;

//     @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
//     private String description;

//     @NotNull(message = "El precio es obligatorio")
//     @Positive(message = "El precio debe ser positivo")
//     private Double price;

//     @Min(value = 0, message = "El stock no puede ser negativo")
//     private Integer stock;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "category_id", nullable = false)
//     @ToString.Exclude
//     private Category category;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "subcategory_id", nullable = false)
//     @ToString.Exclude
//     private Subcategory subcategory;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "brand_id", nullable = false)
//     @ToString.Exclude
//     private Brand brand;

//     @Enumerated(EnumType.STRING)
//     private Status status;

//     @Lob
//     @Basic(fetch = FetchType.LAZY)
//     @ToString.Exclude
//     private byte[] image;

//     public void markAsDeleted() {
//         this.status = Status.DELETED;
//     }

//     public enum Status {
//         ACTIVE,
//         DELETED
//     }
// }
