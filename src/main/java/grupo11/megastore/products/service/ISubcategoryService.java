package grupo11.megastore.products.service;

import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.dto.SubcategoryDTO;
import java.util.List;

public interface ISubcategoryService {
    List<Subcategory> listAll();
    Subcategory getById(Integer id);
    Subcategory save(SubcategoryDTO subcategoryDTO);
    Subcategory update(Integer id, SubcategoryDTO subcategoryDTO);
    void delete(Integer id);
    boolean isNameExists(String name);
}
