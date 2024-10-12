package grupo11.megastore.products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import grupo11.megastore.products.interfaces.ICategoryService;
import grupo11.megastore.products.dto.category.CategoryDTO;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.ICategoryMapper;
import grupo11.megastore.products.dto.category.CreateCategoryDTO;
import grupo11.megastore.products.dto.category.UpdateCategoryDTO;

import java.util.List;
import java.util.ArrayList;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ICategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = this.categoryRepository.findByStatus(EntityStatus.ACTIVE);

        List<CategoryDTO> dtos = new ArrayList<>();
        categories.forEach(category -> {
            dtos.add(this.categoryMapper.categoryToCategoryDTO(category));
        });

        return dtos;
    }

    @Override
    public List<CategoryDTO> getAllDeletedCategories() {
        List<Category> categories = this.categoryRepository.findByStatus(EntityStatus.DELETED);

        List<CategoryDTO> dtos = new ArrayList<>();
        categories.forEach(category -> {
            dtos.add(this.categoryMapper.categoryToCategoryDTO(category));
        });

        return dtos;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = this.categoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id));

        return this.categoryMapper.categoryToCategoryDTO(category);
    }

    @Override
    public CategoryDTO createCategory(CreateCategoryDTO category) {
        if (category.getName() != null && (category.getName().startsWith(" ") || category.getName().endsWith(" "))) {
            throw new APIException("El nombre de la categoría no puede empezar o terminar con espacios");
        }

        this.categoryRepository.findByNameIgnoreCaseAndStatus(category.getName(), EntityStatus.ACTIVE)
                .ifPresent(existingCategory -> {
                    throw new APIException("La categoría ya existe");
                });

        Category entity = new Category();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());

        entity = this.categoryRepository.save(entity);

        return this.categoryMapper.categoryToCategoryDTO(entity);
    }

    @Override
    public CategoryDTO updateCategory(Long id, UpdateCategoryDTO category) {
        Category entity = this.categoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id));

        if (category.isEmpty()) {
            throw new APIException("No se han enviado datos para actualizar");
        }

        if (category.getName() != null) {
            if (category.getName().startsWith(" ") || category.getName().endsWith(" ")) {
                throw new APIException("El nombre de la categoría no puede empezar o terminar con espacios");
            }

            this.categoryRepository.findByNameIgnoreCaseAndStatusAndIdNot(category.getName(), EntityStatus.ACTIVE, id)
                    .ifPresent(existingCategory -> {
                        throw new APIException("La categoría ya existe");
                    });

            entity.setName(category.getName());
        }

        if (category.getDescription() != null) {
            entity.setDescription(category.getDescription());
        }

        entity = this.categoryRepository.save(entity);

        return this.categoryMapper.categoryToCategoryDTO(entity);
    }

    @Override
    public void deleteCategory(Long id) {
        Category entity = this.categoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id));

        entity.delete();

        this.categoryRepository.save(entity);
    }

    @Override
    public void restoreCategory(Long id) {
        Category entity = this.categoryRepository.findByIdAndStatus(id, EntityStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id));

        entity.restore();

        this.categoryRepository.save(entity);
    }
}
