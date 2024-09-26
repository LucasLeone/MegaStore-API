package grupo11.megastore.products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import grupo11.megastore.products.interfaces.ICategoryService;
import grupo11.megastore.products.dto.category.CategoryDTO;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.dto.ICategoryMapper;
import grupo11.megastore.products.dto.category.CreateCategoryDTO;
import grupo11.megastore.products.dto.category.UpdateCategoryDTO;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ICategoryMapper categoryMapper;

    @Override
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = this.categoryRepository.findByStatus(EntityStatus.ACTIVE);

        List<CategoryDTO> dtos = new ArrayList<>();
        categories.forEach(category -> {
            dtos.add(this.categoryMapper.categoryToCategoryDTO(category));
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(Long id) {
        Optional<Category> category = this.categoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE);

        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no existe");
        }

        CategoryDTO dto = this.categoryMapper.categoryToCategoryDTO(category.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CategoryDTO> createCategory(CreateCategoryDTO category) {
        Optional<Category> existingCategory = this.categoryRepository.findByNameAndStatus(category.getName(),
                EntityStatus.ACTIVE);

        if (existingCategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La categoría ya existe");
        }

        Category entity = new Category();
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());

        entity = this.categoryRepository.save(entity);

        CategoryDTO dto = this.categoryMapper.categoryToCategoryDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CategoryDTO> updateCategory(Long id, UpdateCategoryDTO category) {
        Category entity = this.categoryMapper.categoryDTOToCategory(this.getCategoryById(id).getBody());

        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se han enviado datos para actualizar");
        }

        Optional<Category> existingCategory = this.categoryRepository.findByNameAndStatus(category.getName(),
                EntityStatus.ACTIVE);

        if (existingCategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La categoría ya existe");
        }

        if (category.getName() != null) {
            entity.setName(category.getName());
        }

        if (category.getDescription() != null) {
            entity.setDescription(category.getDescription());
        }

        entity = this.categoryRepository.save(entity);

        CategoryDTO dto = this.categoryMapper.categoryToCategoryDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteCategory(Long id) {
        Category entity = this.categoryMapper.categoryDTOToCategory(this.getCategoryById(id).getBody());

        if (entity.getStatus() == EntityStatus.DELETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría no existe");
        }

        entity.delete();

        this.categoryRepository.save(entity);
    }
}
