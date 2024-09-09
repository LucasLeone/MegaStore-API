package grupo11.megastore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import grupo11.megastore.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByEstado(int estado);
}
