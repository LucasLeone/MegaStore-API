package grupo11.megastore.products.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<SubcategoryDTO> getAllSubcategories(Long categoryId) {
        List<Subcategory> subcategories;

        if (categoryId != null) {
            this.categoryRepository.findByIdAndStatus(categoryId, EntityStatus.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", categoryId));

            subcategories = this.subcategoryRepository.findByCategoryIdAndStatus(categoryId, EntityStatus.ACTIVE);
        } else {
            subcategories = this.subcategoryRepository.findByStatus(EntityStatus.ACTIVE);
        }

        List<SubcategoryDTO> dtos = new ArrayList<>();
        subcategories.forEach(subcategory -> {
            dtos.add(subcategoryMapper.subcategoryToSubcategoryDTO(subcategory));
        });

        return dtos;
    }

    @Override
    public List<SubcategoryDTO> getAllDeletedSubcategories() {
        List<Subcategory> subcategories = this.subcategoryRepository.findByStatus(EntityStatus.DELETED);

        List<SubcategoryDTO> dtos = new ArrayList<>();
        subcategories.forEach(subcategory -> {
            dtos.add(subcategoryMapper.subcategoryToSubcategoryDTO(subcategory));
        });

        return dtos;
    }

    @Override
    public SubcategoryDTO getSubcategoryById(Long id) {
        Subcategory subcategory = this.subcategoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría", "id", id));

        return this.subcategoryMapper.subcategoryToSubcategoryDTO(subcategory);
    }

    @Override
    public SubcategoryDTO createSubcategory(CreateSubcategoryDTO subcategory) {
        if (subcategory.getName() != null && (subcategory.getName().startsWith(" ") || subcategory.getName().endsWith(" "))) {
            throw new APIException("El nombre de la subcategoría no puede empezar o terminar con espacios");
        }

        this.subcategoryRepository.findByNameIgnoreCaseAndStatus(subcategory.getName(), EntityStatus.ACTIVE)
                .ifPresent(existingSubcategory -> {
                    throw new APIException("La subcategoría ya existe");
                });

        Category category = this.categoryRepository.findByIdAndStatus(subcategory.getCategoryId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", subcategory.getCategoryId()));

        Subcategory entity = new Subcategory();
        entity.setName(subcategory.getName());
        entity.setDescription(subcategory.getDescription());
        entity.setCategory(category);

        entity = this.subcategoryRepository.save(entity);

        return this.subcategoryMapper.subcategoryToSubcategoryDTO(entity);
    }

    @Override
    public SubcategoryDTO updateSubcategory(Long id, UpdateSubcategoryDTO subcategory) {
        Subcategory entity = this.subcategoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría", "id", id));

        if (subcategory.isEmpty()) {
            throw new APIException("No se han enviado datos para actualizar");
        }

        if (subcategory.getName() != null) {
            if (subcategory.getName().startsWith(" ") || subcategory.getName().endsWith(" ")) {
                throw new APIException("El nombre de la subcategoría no puede empezar o terminar con espacios");
            }

            this.subcategoryRepository.findByNameIgnoreCaseAndStatusAndIdNot(subcategory.getName(), EntityStatus.ACTIVE, id)
                    .ifPresent(existingSubcategory -> {
                        throw new APIException("La subcategoría ya existe");
                    });

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

        return this.subcategoryMapper.subcategoryToSubcategoryDTO(entity);
    }

    @Override
    public void deleteSubcategory(Long id) {
        Subcategory entity = this.subcategoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría", "id", id));

        entity.delete();

        this.subcategoryRepository.save(entity);
    }
}
