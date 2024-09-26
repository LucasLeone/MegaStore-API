package grupo11.megastore.products.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import grupo11.megastore.products.dto.variant.VariantDTO;
import grupo11.megastore.products.model.Variant;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IVariantMapper {
    
    @Mapping(source = "product.id", target = "productId")
    VariantDTO variantToVariantDTO(Variant variant);

    @Mapping(source = "productId", target = "product.id")
    Variant variantDTOToVariant(VariantDTO variantDTO);
}
