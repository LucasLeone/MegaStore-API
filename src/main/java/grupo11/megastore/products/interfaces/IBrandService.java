package grupo11.megastore.products.interfaces;

import grupo11.megastore.products.dto.brand.BrandDTO;
import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import grupo11.megastore.products.dto.brand.UpdateBrandDTO;

import java.util.List;

public interface IBrandService {
    /**
     * Obtener todas las marcas
     */
    public List<BrandDTO> getAllBrands();

    /**
     * Obtener todas las marcas eliminadas
     */
    public List<BrandDTO> getAllDeletedBrands();

    /**
     * Obtener una marca por ID
     */
    public BrandDTO getBrandById(Long id);

    /**
     * Crear una nueva marca
     */
    public BrandDTO createBrand(CreateBrandDTO brand);

    /**
     * Actualizar una marca
     */
    public BrandDTO updateBrand(Long id, UpdateBrandDTO brand);

    /**
     * Eliminar una marca
     */
    public void deleteBrand(Long id);
}
