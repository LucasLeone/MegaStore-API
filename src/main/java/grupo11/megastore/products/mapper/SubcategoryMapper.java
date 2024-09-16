package grupo11.megastore.products.mapper;

import grupo11.megastore.products.dto.SubcategoryDTO;
import grupo11.megastore.products.model.Subcategory;

public class SubcategoryMapper {
    public static SubcategoryDTO toDTO(Subcategory subcategory) {
        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(subcategory.getId());
        dto.setName(subcategory.getName());
        dto.setDescription(subcategory.getDescription());
        return dto;
    }

    public static Subcategory toEntity(SubcategoryDTO dto) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(dto.getId());
        subcategory.setName(dto.getName());
        subcategory.setDescription(dto.getDescription());
        return subcategory;
    }
}
