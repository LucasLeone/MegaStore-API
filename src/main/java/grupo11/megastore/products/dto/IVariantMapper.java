package grupo11.megastore.products.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import grupo11.megastore.products.dto.variant.VariantDTO;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.dto.IProductMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, 
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = { IProductMapper.class })
public interface IVariantMapper {
    
    @Mapping(source = "product", target = "product")
    VariantDTO variantToVariantDTO(Variant variant);

    @Mapping(target = "product", ignore = true)
    Variant variantDTOToVariant(VariantDTO variantDTO);
}
