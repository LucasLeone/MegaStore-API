package grupo11.megastore.sales.specification;

import org.springframework.data.jpa.domain.Specification;
import grupo11.megastore.sales.model.Sale;
import grupo11.megastore.sales.model.SaleDetail;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.users.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import java.time.LocalDateTime;
import java.util.List;

public class SaleSpecifications {

    public static Specification<Sale> hasSaleDateAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("saleDate"), date);
    }

    public static Specification<Sale> hasSaleDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("saleDate"), start, end);
    }

    public static Specification<Sale> hasBrandIn(List<Long> brands) {
        return (root, query, criteriaBuilder) -> {
            if (brands == null || brands.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Sale, SaleDetail> saleDetailJoin = root.join("saleDetails", JoinType.LEFT);
            Join<SaleDetail, Product> productJoin = saleDetailJoin.join("variant").join("product");
            return productJoin.get("brand").get("id").in(brands);
        };
    }

    public static Specification<Sale> hasCategoryIn(List<Long> categories) {
        return (root, query, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Sale, SaleDetail> saleDetailJoin = root.join("saleDetails", JoinType.LEFT);
            Join<SaleDetail, Product> productJoin = saleDetailJoin.join("variant").join("product");
            return productJoin.get("category").get("id").in(categories);
        };
    }

    public static Specification<Sale> hasSubcategoryIn(List<Long> subcategories) {
        return (root, query, criteriaBuilder) -> {
            if (subcategories == null || subcategories.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Sale, SaleDetail> saleDetailJoin = root.join("saleDetails", JoinType.LEFT);
            Join<SaleDetail, Product> productJoin = saleDetailJoin.join("variant").join("product");
            return productJoin.get("subcategory").get("id").in(subcategories);
        };
    }

    public static Specification<Sale> hasCustomerId(Long customerId) {
        return (root, query, criteriaBuilder) -> {
            if (customerId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Sale, User> userJoin = root.join("user", JoinType.LEFT);
            return criteriaBuilder.equal(userJoin.get("id"), customerId);
        };
    }
}
