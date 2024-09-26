package grupo11.megastore.products.specification;

import org.springframework.data.jpa.domain.Specification;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Variant;

/**
 * Specifications for filtering Variant entities based on various criteria.
 */
public class VariantSpecification {

    /**
     * Specification to filter variants by status.
     * 
     * @param status The status to filter by.
     * @return A Specification for filtering by status.
     */
    public static Specification<Variant> hasStatus(EntityStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    /**
     * Specification to filter variants by product ID.
     * 
     * @param productId The product ID to filter by.
     * @return A Specification for filtering by product ID.
     */
    public static Specification<Variant> hasProductId(Long productId) {
        return (root, query, criteriaBuilder) -> 
            productId == null ? null : criteriaBuilder.equal(root.get("product").get("id"), productId);
    }

    /**
     * Specification to filter variants whose color contains a specific string (case-insensitive).
     * 
     * @param color The string to search within the color.
     * @return A Specification for partial matching on color.
     */
    public static Specification<Variant> colorContains(String color) {
        return (root, query, criteriaBuilder) -> 
            color == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("color")), "%" + color.toLowerCase() + "%");
    }

    /**
     * Specification to filter variants whose size contains a specific string (case-insensitive).
     * 
     * @param size The string to search within the size.
     * @return A Specification for partial matching on size.
     */
    public static Specification<Variant> sizeContains(String size) {
        return (root, query, criteriaBuilder) -> 
            size == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("size")), "%" + size.toLowerCase() + "%");
    }
}
