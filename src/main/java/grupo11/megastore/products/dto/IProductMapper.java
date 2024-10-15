package grupo11.megastore.products.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import grupo11.megastore.products.dto.product.ProductDTO;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.dto.ICategoryMapper;
import grupo11.megastore.products.dto.ISubcategoryMapper;
import grupo11.megastore.products.dto.IBrandMapper;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = { ICategoryMapper.class, ISubcategoryMapper.class, IBrandMapper.class }
)
public interface IProductMapper {
    
    ProductDTO productToProductDTO(Product product);

    Product productDTOToProduct(ProductDTO productDTO);
}
