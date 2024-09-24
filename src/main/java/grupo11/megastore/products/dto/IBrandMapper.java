package grupo11.megastore.products.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import grupo11.megastore.products.dto.brand.BrandDTO;
import grupo11.megastore.products.model.Brand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IBrandMapper {
    BrandDTO brandToBrandDTO(Brand brand);

    Brand brandDTOToBrand(BrandDTO brandDTO);
}
