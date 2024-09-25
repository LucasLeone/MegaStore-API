package grupo11.megastore.products.specification;

import org.springframework.data.jpa.domain.Specification;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.model.Product;

public class ProductSpecification {

    public static Specification<Product> hasStatus(EntityStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> 
            categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> hasSubcategoryId(Long subcategoryId) {
        return (root, query, criteriaBuilder) -> 
            subcategoryId == null ? null : criteriaBuilder.equal(root.get("subcategory").get("id"), subcategoryId);
    }

    public static Specification<Product> hasBrandId(Long brandId) {
        return (root, query, criteriaBuilder) -> 
            brandId == null ? null : criteriaBuilder.equal(root.get("brand").get("id"), brandId);
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder) -> 
            name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
