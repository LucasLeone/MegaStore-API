package grupo11.megastore.products.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import grupo11.megastore.products.dto.subcategory.SubcategoryDTO;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.dto.ICategoryMapper;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = { ICategoryMapper.class }
)
public interface ISubcategoryMapper {

    @Mapping(source = "category", target = "category")
    SubcategoryDTO subcategoryToSubcategoryDTO(Subcategory subcategory);

    @Mapping(target = "category", ignore = true)
    Subcategory subcategoryDTOToSubcategory(SubcategoryDTO subcategoryDTO);
}
