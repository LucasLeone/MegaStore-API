package grupo11.megastore.products.service;

import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> listAll() {
        return categoryRepository.findByStatus(Category.ACTIVE);
    }

    @Override
    public Category getById(Integer id) {
        return categoryRepository.findById(id)
                .filter(category -> category.getStatus() == Category.ACTIVE)
                .orElse(null);
    }

    @Override
    public Category save(Category category) {
        category.setStatus(Category.ACTIVE);
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        if (!categoryRepository.existsById(category.getId())) {
            throw new ResourceNotFoundException("Category with id " + category.getId() + " not found.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Integer id) {
        Category category = getById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found.");
        }
        category.markAsDeleted();
        categoryRepository.save(category);
    }

    public boolean isNameExists(String name) {
        return categoryRepository.findByName(name).isPresent();
    }
}
