package grupo11.megastore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;


@Entity
@Data
@ToString
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String descripcion;
    
    private int estado;
    public static final int COMUN=0;
    public static final int ELIMINADO=1;

    public void asEliminar() {
        this.setEstado(1);
    }
}
