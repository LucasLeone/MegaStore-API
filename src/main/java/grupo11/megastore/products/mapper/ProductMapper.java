package grupo11.megastore.products.mapper;

import org.springframework.stereotype.Component;

import grupo11.megastore.products.dto.ProductDTO;
import grupo11.megastore.products.model.Product;

import java.util.Base64;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getImageBase64() != null && !dto.getImageBase64().isEmpty()) {
            product.setImage(Base64.getDecoder().decode(dto.getImageBase64()));
        }

        if (dto.getStatus() != null) {
            product.setStatus(Product.Status.valueOf(dto.getStatus()));
        }

        return product;
    }

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        dto.setCategoryId(product.getCategory().getId());
        dto.setSubcategoryId(product.getSubcategory().getId());
        dto.setBrandId(product.getBrand().getId());

        if (product.getStatus() != null) {
            dto.setStatus(product.getStatus().name());
        }

        if (product.getImage() != null) {
            dto.setImageBase64(Base64.getEncoder().encodeToString(product.getImage()));
        }

        return dto;
    }
}
