package grupo11.megastore.products.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import java.util.Set;

@Entity
@Data
@ToString(exclude = "subcategories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;

    @NotNull
    private int status;
    public static final int ACTIVE = 0;
    public static final int DELETED = 1;

    public void markAsDeleted() {
        this.setStatus(DELETED);
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subcategory> subcategories;
}
