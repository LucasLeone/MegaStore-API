package grupo11.megastore.products.service;

import grupo11.megastore.products.model.Subcategory;

import java.util.List;

public interface ISubcategoryService {
    List<Subcategory> listAll();
    Subcategory getById(Integer id);
    Subcategory save(Subcategory subcategory);
    Subcategory update(Subcategory subcategory);
    void delete(Integer id);
}
