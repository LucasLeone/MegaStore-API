package grupo11.megastore.products.service;

import grupo11.megastore.products.model.Brand;

import java.util.List;

public interface IBrandService {
    List<Brand> listAll();
    Brand getById(Integer id);
    Brand save(Brand brand);
    Brand update(Brand brand);
    void delete(Integer id);
}
