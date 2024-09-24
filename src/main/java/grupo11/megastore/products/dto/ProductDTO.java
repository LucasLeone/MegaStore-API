// package grupo11.megastore.products.dto;

// import jakarta.validation.constraints.*;
// import lombok.Data;

// @Data
// public class ProductDTO {
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

//     @NotNull(message = "La categoría es obligatoria")
//     private Integer categoryId;

//     @NotNull(message = "La subcategoría es obligatoria")
//     private Integer subcategoryId;

//     @NotNull(message = "La marca es obligatoria")
//     private Integer brandId;

//     private String status;

//     private String imageBase64;
// }
