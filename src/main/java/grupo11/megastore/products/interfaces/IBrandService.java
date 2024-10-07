package grupo11.megastore.products.interfaces;

import grupo11.megastore.products.dto.brand.BrandDTO;
import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import grupo11.megastore.products.dto.brand.UpdateBrandDTO;

import java.util.List;

public interface IBrandService {
    /**
     * Get all brands
     * 
     * @return
     */
    public List<BrandDTO> getAllBrands();

    /**
     * Get all deleted brands
     * 
     * @return
     */
    public List<BrandDTO> getAllDeletedBrands();

    /**
     * Get a brand by id
     * 
     * @param id
     * 
     * @return
     */
    public BrandDTO getBrandById(Long id);

    /**
     * Create a new brand
     * 
     * @param brand
     * 
     * @return
     */
    public BrandDTO createBrand(CreateBrandDTO brand);

    /**
     * Update a brand
     * 
     * @param id
     * @param brand
     * 
     * @return
     */
    public BrandDTO updateBrand(Long id, UpdateBrandDTO brand);

    /**
     * Delete a brand
     * 
     * @param id
     * 
     * @return
     */
    public void deleteBrand(Long id);
}
