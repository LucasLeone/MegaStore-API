package grupo11.megastore.products.interfaces;

import grupo11.megastore.products.dto.variant.VariantDTO;
import grupo11.megastore.products.dto.variant.CreateVariantDTO;
import grupo11.megastore.products.dto.variant.UpdateVariantDTO;

import java.util.List;

public interface IVariantService {
    /**
     * Get all variants
     * 
     * @param productId
     * @param color
     * @param size
     * 
     * @return
     */
    List<VariantDTO> getAllVariants(Long productId, String color, String size);

    /**
     * Get all deleted variants
     * 
     * @return
     */
    List<VariantDTO> getAllDeletedVariants();
    
    /**
     * Get a variant by id
     * 
     * @param id
     * 
     * @return
     */
    VariantDTO getVariantById(Long id);
    
    /**
     * Create a new variant
     * 
     * @param variant
     * 
     * @return
     */
    VariantDTO createVariant(CreateVariantDTO variant);

    /**
     * Update a variant
     * 
     * @param id
     * @param variant
     * 
     * @return
     */
    VariantDTO updateVariant(Long id, UpdateVariantDTO variant);

    /**
     * Delete a variant
     * 
     * @param id
     * 
     * @return
     */
    void deleteVariant(Long id);

    /**
     * Restore a variant
     * 
     * @param id
     * 
     * @return
     */
    void restoreVariant(Long id);
}
