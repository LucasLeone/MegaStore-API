// package grupo11.megastore.products.model;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.validation.constraints.NotNull;
// import lombok.Data;
// import lombok.ToString;

// @Entity
// @Data
// @ToString
// public class Subcategory {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer id;

//     @Column(nullable = false, unique = true)
//     private String name;
//     private String description;

//     @ManyToOne(optional = false, fetch = FetchType.LAZY)
//     @JoinColumn(name = "category_id", nullable = false)
//     private Category category;

//     @NotNull
//     private int status;
//     public static final int ACTIVE = 0;
//     public static final int DELETED = 1;

//     public void markAsDeleted() {
//         this.setStatus(DELETED);
//     }
// }
