package grupo11.megastore.products.service;

import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.repository.SubcategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService implements ISubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Override
    public List<Subcategory> listAll() {
        return subcategoryRepository.findByStatus(Subcategory.ACTIVE);
    }

    @Override
    public Subcategory getById(Integer id) {
        return subcategoryRepository.findById(id)
                .filter(subcategory -> subcategory.getStatus() == Subcategory.ACTIVE)
                .orElse(null);
    }

    @Override
    public Subcategory save(Subcategory subcategory) {
        subcategory.setStatus(Subcategory.ACTIVE);
        return subcategoryRepository.save(subcategory);
    }

    @Override
    public Subcategory update(Subcategory subcategory) {
        if (!subcategoryRepository.existsById(subcategory.getId())) {
            throw new ResourceNotFoundException("Subcategory with id " + subcategory.getId() + " not found.");
        }
        return subcategoryRepository.save(subcategory);
    }

    @Override
    public void delete(Integer id) {
        Subcategory subcategory = getById(id);
        if (subcategory == null) {
            throw new ResourceNotFoundException("Subcategory with id " + id + " not found.");
        }
        subcategory.markAsDeleted();
        subcategoryRepository.save(subcategory);
    }

    public boolean isNameExists(String name) {
        return subcategoryRepository.findByName(name).isPresent();
    }
}
