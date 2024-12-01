package grupo11.megastore.sales.specification;

import org.springframework.data.jpa.domain.Specification;
import grupo11.megastore.sales.model.Sale;
import grupo11.megastore.sales.model.SaleDetail;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.users.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import java.time.LocalDateTime;

public class SaleSpecifications {

    public static Specification<Sale> hasSaleDateAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("saleDate"), date);
    }

    public static Specification<Sale> hasSaleDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("saleDate"), start, end);
    }

    public static Specification<Sale> hasBrandIn(Long brand) {
        return (root, query, criteriaBuilder) -> {
            if (brand == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Sale, SaleDetail> saleDetailJoin = root.join("saleDetails", JoinType.LEFT);
            Join<SaleDetail, Product> productJoin = saleDetailJoin.join("variant").join("product");
            return productJoin.get("brand").get("id").in(brand);
        };
    }

    public static Specification<Sale> hasCategoryIn(Long category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Sale, SaleDetail> saleDetailJoin = root.join("saleDetails", JoinType.LEFT);
            Join<SaleDetail, Product> productJoin = saleDetailJoin.join("variant").join("product");
            return productJoin.get("category").get("id").in(category);
        };
    }

    public static Specification<Sale> hasSubcategoryIn(Long subcategory) {
        return (root, query, criteriaBuilder) -> {
            if (subcategory == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Sale, SaleDetail> saleDetailJoin = root.join("saleDetails", JoinType.LEFT);
            Join<SaleDetail, Product> productJoin = saleDetailJoin.join("variant").join("product");
            return productJoin.get("subcategory").get("id").in(subcategory);
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
