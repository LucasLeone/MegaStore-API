package grupo11.megastore.products.model;

import grupo11.megastore.constant.EntityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 32)
    private String name;

    @Column(nullable = true)
    @Size(max = 128)
    private String description;

    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    public void delete() {
        this.setStatus(EntityStatus.DELETED);;
    }
    
    public void restore() {
        this.setStatus(EntityStatus.ACTIVE);
    }
}
