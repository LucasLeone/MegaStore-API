package grupo11.megastore.products.mapper;

import grupo11.megastore.products.dto.BrandDTO;
import grupo11.megastore.products.model.Brand;

public class BrandMapper {
    public static BrandDTO toDTO(Brand brand) {
        BrandDTO dto = new BrandDTO();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setDescription(brand.getDescription());
        return dto;
    }

    public static Brand toEntity(BrandDTO dto) {
        Brand brand = new Brand();
        brand.setId(dto.getId());
        brand.setName(dto.getName());
        brand.setDescription(dto.getDescription());
        return brand;
    }
}
