package grupo11.megastore.products.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Product;

/**
 * Repository interface for managing Product entities.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    /**
     * Find a product by its ID and status.
     * 
     * @param id     The ID of the product to search for.
     * @param status The status of the product to filter by.
     * 
     * @return       An Optional containing the product if found.
     */
    Optional<Product> findByIdAndStatus(Long id, EntityStatus status);

    /**
     * Find a product by its name and status.
     * 
     * @param name   The name of the product to search for.
     * @param status The status of the product to filter by.
     * 
     * @return       An Optional containing the product if found.
     */
    Optional<Product> findByNameAndStatus(String name, EntityStatus status);

    /**
     * Find a product by its name and status, excluding the product with the specified ID.
     * 
     * @param name   The name of the product to search for.
     * @param status The status of the product to filter by.
     * @param id     The ID of the product to exclude from the search.
     * 
     * @return       An Optional containing the product if found.
     */
    Optional<Product> findByNameAndStatusAndIdNot(String name, EntityStatus status, Long id);

    /**
     * Find all products by their status.
     * 
     * @param status The status to filter products by.
     * 
     * @return       A list of products matching the specified status.
     */
    List<Product> findByStatus(EntityStatus status);
    
    /**
     * Find all products associated with a specific category ID and status.
     * 
     * @param categoryId The ID of the category to which the products belong.
     * @param status     The status to filter products by.
     * @return           A list of products belonging to the specified category and matching the status.
     */
    List<Product> findByCategoryIdAndStatus(Long categoryId, EntityStatus status);
    
    /**
     * Find all products associated con una subcategoría específica y estado.
     * 
     * @param subcategoryId El ID de la subcategoría a la que pertenecen los productos.
     * @param status        El estado para filtrar los productos.
     * @return              Una lista de productos que pertenecen a la subcategoría especificada y que coinciden con el estado.
     */
    List<Product> findBySubcategoryIdAndStatus(Long subcategoryId, EntityStatus status);
    
    /**
     * Find all products associated con una marca específica y estado.
     * 
     * @param brandId El ID de la marca a la que pertenecen los productos.
     * @param status  El estado para filtrar los productos.
     * @return        Una lista de productos que pertenecen a la marca especificada y que coinciden con el estado.
     */
    List<Product> findByBrandIdAndStatus(Long brandId, EntityStatus status);
    
    /**
     * (Opcional) Buscar productos cuyo nombre contenga una cadena específica y tengan un estado dado.
     * 
     * @param name   La cadena a buscar dentro del nombre del producto.
     * @param status El estado para filtrar los productos.
     * @return       Una lista de productos que contienen la cadena especificada en su nombre y que coinciden con el estado.
     */
    List<Product> findByNameContainingAndStatus(String name, EntityStatus status);
}
