package grupo11.megastore.products.service;

import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.repository.BrandRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService implements IBrandService {
    
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> listAll() {
        return brandRepository.findByStatus(Brand.ACTIVE);
    }

    @Override
    public Brand getById(Integer id) {
        return brandRepository.findById(id)
                .filter(brand -> brand.getStatus() == Brand.ACTIVE)
                .orElse(null);
    }

    @Override
    public Brand save(Brand brand) {
        brand.setStatus(Brand.ACTIVE);
        return brandRepository.save(brand);
    }

    @Override
    public Brand update(Brand brand) {
        if (!brandRepository.existsById(brand.getId())) {
            throw new ResourceNotFoundException("Brand with id " + brand.getId() + " not found.");
        }
        return brandRepository.save(brand);
    }

    @Override
    public void delete(Integer id) {
        Brand brand = getById(id);
        if (brand == null) {
            throw new ResourceNotFoundException("Brand with id " + id + " not found.");
        }
        brand.markAsDeleted();
        brandRepository.save(brand);
    }
}
