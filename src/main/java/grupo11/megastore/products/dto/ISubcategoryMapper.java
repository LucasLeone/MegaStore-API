package grupo11.megastore.products.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import grupo11.megastore.products.dto.subcategory.SubcategoryDTO;
import grupo11.megastore.products.model.Subcategory;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ISubcategoryMapper {

    @Mapping(source = "category.id", target = "categoryId")
    SubcategoryDTO subcategoryToSubcategoryDTO(Subcategory subcategory);

    @Mapping(source = "categoryId", target = "category.id")
    Subcategory subcategoryDTOToSubcategory(SubcategoryDTO subcategoryDTO);
}
