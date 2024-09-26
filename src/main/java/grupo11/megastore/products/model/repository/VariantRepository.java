package grupo11.megastore.products.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Variant;

/**
 * Repository interface for managing Variant entities.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 * Extends JpaSpecificationExecutor to support dynamic query construction.
 */
public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant> {
    
    /**
     * Find a variant by its ID and status.
     * 
     * @param id     The ID of the variant to search for.
     * @param status The status of the variant to filter by.
     * 
     * @return       An Optional containing the variant if found.
     */
    Optional<Variant> findByIdAndStatus(Long id, EntityStatus status);

    /**
     * Find variants by product ID and status.
     * 
     * @param productId The ID of the product to which the variants belong.
     * @param status    The status to filter variants by.
     * 
     * @return          A list of variants matching the criteria.
     */
    List<Variant> findByProductIdAndStatus(Long productId, EntityStatus status);

    /**
     * Find variants by color and status.
     * 
     * @param color  The color to filter variants by.
     * @param status The status to filter variants by.
     * 
     * @return       A list of variants matching the criteria.
     */
    List<Variant> findByColorAndStatus(String color, EntityStatus status);

    /**
     * Find variants by size and status.
     * 
     * @param size   The size to filter variants by.
     * @param status The status to filter variants by.
     * 
     * @return       A list of variants matching the criteria.
     */
    List<Variant> findBySizeAndStatus(String size, EntityStatus status);

    /**
     * Find variants by product ID, color, size, and status.
     * 
     * @param productId The ID of the product.
     * @param color     The color to filter by.
     * @param size      The size to filter by.
     * @param status    The status to filter by.
     * 
     * @return          A list of variants matching the criteria.
     */
    Optional<Variant> findByProductIdAndColorAndSizeAndStatus(Long productId, String color, String size, EntityStatus status);

    /**
     * Find variants by product ID, color, size, status, and excluding a specific ID.
     * 
     * @param productId The ID of the product.
     * @param color     The color to filter by.
     * @param size      The size to filter by.
     * @param status    The status to filter by.
     * @param id        The ID to exclude from the search.
     * 
     * @return          An Optional containing the variant if found.
     */
    Optional<Variant> findByProductIdAndColorAndSizeAndStatusAndIdNot(Long productId, String color, String size, EntityStatus status, Long id);

    /**
     * (Opcional) Buscar variantes cuyo color contenga una cadena específica y tengan un estado dado.
     * 
     * @param color  La cadena a buscar dentro del color de la variante.
     * @param status El estado para filtrar las variantes.
     * 
     * @return       Una lista de variantes que contienen la cadena especificada en su color y que coinciden con el estado.
     */
    List<Variant> findByColorContainingAndStatus(String color, EntityStatus status);

    /**
     * (Opcional) Buscar variantes cuyo tamaño contenga una cadena específica y tengan un estado dado.
     * 
     * @param size   La cadena a buscar dentro del tamaño de la variante.
     * @param status El estado para filtrar las variantes.
     * 
     * @return       Una lista de variantes que contienen la cadena especificada en su tamaño y que coinciden con el estado.
     */
    List<Variant> findBySizeContainingAndStatus(String size, EntityStatus status);
}
