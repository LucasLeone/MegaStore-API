package grupo11.megastore.products.controller;

import java.util.List;
import java.util.stream.Collectors;

import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.CategoryDTO;
import grupo11.megastore.products.mapper.CategoryMapper;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.service.ICategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listAll() {
        List<Category> categories = categoryService.listAll();
        List<CategoryDTO> categoriesDTO = categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoriesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Integer id) {
        Category category = categoryService.getById(id);

        if (category == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found.");
        }

        CategoryDTO categoryDTO = CategoryMapper.toDTO(category);
        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryService.save(category);
        CategoryDTO savedCategoryDTO = CategoryMapper.toDTO(savedCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDTO> updatePartial(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        Category existingCategory = categoryService.getById(id);
        if (existingCategory == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found.");
        }

        if (categoryDTO.getName() != null) {
            existingCategory.setName(categoryDTO.getName());
        }

        if (categoryDTO.getDescription() != null) {
            existingCategory.setDescription(categoryDTO.getDescription());
        }

        Category updatedCategory = categoryService.update(existingCategory);
        CategoryDTO updatedCategoryDTO = CategoryMapper.toDTO(updatedCategory);

        return ResponseEntity.ok(updatedCategoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found.");
        }

        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
