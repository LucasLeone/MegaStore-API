package grupo11.megastore.sales.model;

import grupo11.megastore.sales.constant.SaleStatus;
import grupo11.megastore.users.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "shipping_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "shipping_method", nullable = false)
    private String shippingMethod;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetail> saleDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false)
    private SaleStatus status = SaleStatus.IN_PROCESS;

    public void markAsSent() {
        this.setStatus(SaleStatus.SENT);
    }

    public void markAsCompleted() {
        this.setStatus(SaleStatus.COMPLETED);
    }

    public void markAsCanceled() {
        this.setStatus(SaleStatus.CANCELED);
    }

    public void markIsInProcess() {
        this.setStatus(SaleStatus.IN_PROCESS);
    }

    public void addSaleDetail(SaleDetail detail) {
        saleDetails.add(detail);
        detail.setSale(this);
        calculateTotalAmount();
    }

    public void removeSaleDetail(SaleDetail detail) {
        saleDetails.remove(detail);
        detail.setSale(null);
        calculateTotalAmount();
    }

    public void calculateTotalAmount() {
        this.totalAmount = saleDetails.stream()
                .map(SaleDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalAmount = this.totalAmount.add(this.shippingCost);
    }
}
