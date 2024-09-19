package grupo11.megastore.products.service;

import grupo11.megastore.products.dto.ProductDTO;
import java.util.List;

public interface IProductService {
    List<ProductDTO> listAll();
    ProductDTO getById(Integer id);
    ProductDTO save(ProductDTO productDTO);
    ProductDTO update(ProductDTO productDTO);
    void delete(Integer id);
    boolean isNameExists(String name);
}
