package grupo11.megastore.products.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import grupo11.megastore.products.dto.product.ProductDTO;
import grupo11.megastore.products.model.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IProductMapper {
    
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "subcategory.id", target = "subcategoryId")
    @Mapping(source = "brand.id", target = "brandId")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "subcategoryId", target = "subcategory.id")
    @Mapping(source = "brandId", target = "brand.id")
    Product productDTOToProduct(ProductDTO productDTO);
}
