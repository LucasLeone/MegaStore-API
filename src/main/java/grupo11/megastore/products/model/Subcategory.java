package grupo11.megastore.products.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    @NotNull
    private int status;
    public static final int ACTIVE = 0;
    public static final int DELETED = 1;

    public void markAsDeleted() {
        this.setStatus(DELETED);
    }
}
