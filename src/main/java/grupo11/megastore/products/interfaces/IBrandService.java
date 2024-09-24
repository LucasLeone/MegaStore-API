package grupo11.megastore.products.interfaces;

import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<List<BrandDTO>> getAllBrands();


    /**
     * Get a brand by id
     * 
     * @param id
     * 
     * @return
     */
    public ResponseEntity<BrandDTO> getBrandById(Long id);


    /**
     * Create a new brand
     * 
     * @param brand
     * 
     * @return
     */
    public ResponseEntity<BrandDTO> createBrand(CreateBrandDTO brand);

    /**
     * Update a brand
     * 
     * @param id
     * @param brand
     * 
     * @return
     */
    public ResponseEntity<BrandDTO> updateBrand(Long id, UpdateBrandDTO brand);

    /**
     * Delete a brand
     * 
     * @param id
     * 
     * @return
     */
    public void deleteBrand(Long id);
}
