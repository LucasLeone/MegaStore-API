package grupo11.megastore.products.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.ISubcategoryMapper;
import grupo11.megastore.products.dto.subcategory.CreateSubcategoryDTO;
import grupo11.megastore.products.dto.subcategory.SubcategoryDTO;
import grupo11.megastore.products.dto.subcategory.UpdateSubcategoryDTO;
import grupo11.megastore.products.interfaces.ISubcategoryService;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.model.repository.SubcategoryRepository;

@Service
public class SubcategoryService implements ISubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ISubcategoryMapper subcategoryMapper;

    @Override
    public ResponseEntity<List<SubcategoryDTO>> getAllSubcategories(Long categoryId) {
        this.categoryRepository.findByIdAndStatus(categoryId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", categoryId));

        List<Subcategory> subcategories = this.subcategoryRepository.findByCategoryIdAndStatus(categoryId, EntityStatus.ACTIVE);

        List<SubcategoryDTO> dtos = new ArrayList<>();
        subcategories.forEach(subcategory -> {
            dtos.add(this.subcategoryMapper.subcategoryToSubcategoryDTO(subcategory));
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(Long id) {
        Subcategory subcategory = this.subcategoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new APIException("La subcategoría no existe"));

        SubcategoryDTO dto = this.subcategoryMapper.subcategoryToSubcategoryDTO(subcategory);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubcategoryDTO> createSubcategory(CreateSubcategoryDTO subcategory) {
        this.subcategoryRepository.findByNameIgnoreCaseAndStatus(subcategory.getName(), EntityStatus.ACTIVE)
                .ifPresent(existingSubcategory -> {
                    throw new APIException("La subcategorìa ya existe");
                });

        Category category = this.categoryRepository.findByIdAndStatus(subcategory.getCategoryId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", subcategory.getCategoryId()));

        Subcategory entity = new Subcategory();
        entity.setName(subcategory.getName());
        entity.setDescription(subcategory.getDescription());
        entity.setCategory(category);

        entity = this.subcategoryRepository.save(entity);

        SubcategoryDTO dto = this.subcategoryMapper.subcategoryToSubcategoryDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SubcategoryDTO> updateSubcategory(Long id, UpdateSubcategoryDTO subcategory) {
        Subcategory entity = this.subcategoryMapper.subcategoryDTOToSubcategory(this.getSubcategoryById(id).getBody());

        if (subcategory.isEmpty()) {
            throw new APIException("No se han enviado datos para actualizar");
        }

        this.subcategoryRepository.findByNameIgnoreCaseAndStatusAndIdNot(subcategory.getName(), EntityStatus.ACTIVE, id)
                .ifPresent(existingSubcategory -> {
                    throw new APIException("La subcategorìa ya existe");
                });

        if (subcategory.getName() != null) {
            entity.setName(subcategory.getName());
        }

        if (subcategory.getDescription() != null) {
            entity.setDescription(subcategory.getDescription());
        }

        if (subcategory.getCategoryId() != null) {
            Category category = this.categoryRepository
                    .findByIdAndStatus(subcategory.getCategoryId(), EntityStatus.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", subcategory.getCategoryId()));

            entity.setCategory(category);
        }

        entity = this.subcategoryRepository.save(entity);

        SubcategoryDTO dto = this.subcategoryMapper.subcategoryToSubcategoryDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteSubcategory(Long id) {
        Subcategory entity = this.subcategoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría", "id", id));

        entity.delete();

        this.subcategoryRepository.save(entity);
    }
}
