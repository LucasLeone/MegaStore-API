package grupo11.megastore.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import grupo11.megastore.constant.EntityStatus;
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
    public ResponseEntity<List<SubcategoryDTO>> getAllSubcategories() {
        List<Subcategory> subcategories = this.subcategoryRepository.findByStatus(EntityStatus.ACTIVE);

        List<SubcategoryDTO> dtos = new ArrayList<>();
        subcategories.forEach(subcategory -> {
            dtos.add(this.subcategoryMapper.subcategoryToSubcategoryDTO(subcategory));
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(Long id) {
        Optional<Subcategory> subcategory = this.subcategoryRepository.findByIdAndStatus(id, EntityStatus.ACTIVE);

        if (!subcategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La subcategoría no existe");
        }

        SubcategoryDTO dto = this.subcategoryMapper.subcategoryToSubcategoryDTO(subcategory.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubcategoryDTO> createSubcategory(CreateSubcategoryDTO subcategory) {
        Optional<Subcategory> existingSubcategory = this.subcategoryRepository
                .findByNameAndStatus(subcategory.getName(), EntityStatus.ACTIVE);

        if (existingSubcategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La subcategoría ya existe");
        }

        Optional<Category> categoryOpt = this.categoryRepository.findByIdAndStatus(subcategory.getCategoryId(),
                EntityStatus.ACTIVE);

        if (!categoryOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no existe");
        }

        Subcategory entity = new Subcategory();
        entity.setName(subcategory.getName());
        entity.setDescription(subcategory.getDescription());
        entity.setCategory(categoryOpt.get());

        entity = this.subcategoryRepository.save(entity);

        SubcategoryDTO dto = this.subcategoryMapper.subcategoryToSubcategoryDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SubcategoryDTO> updateSubcategory(Long id, UpdateSubcategoryDTO subcategory) {
        Subcategory entity = this.subcategoryMapper.subcategoryDTOToSubcategory(this.getSubcategoryById(id).getBody());

        if (subcategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se han enviado datos para actualizar");
        }

        Optional<Subcategory> existingSubcategory = this.subcategoryRepository
                .findByNameAndStatusAndIdNot(subcategory.getName(), EntityStatus.ACTIVE, id);

        if (existingSubcategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La subcategoría ya existe");
        }

        if (subcategory.getName() != null) {
            entity.setName(subcategory.getName());
        }

        if (subcategory.getDescription() != null) {
            entity.setDescription(subcategory.getDescription());
        }

        if (subcategory.getCategoryId() != null) {
            Optional<Category> categoryOpt = this.categoryRepository.findByIdAndStatus(subcategory.getCategoryId(),
                    EntityStatus.ACTIVE);

            if (!categoryOpt.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no existe");
            }

            entity.setCategory(categoryOpt.get());
        }

        entity = this.subcategoryRepository.save(entity);

        SubcategoryDTO dto = this.subcategoryMapper.subcategoryToSubcategoryDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteSubcategory(Long id) {
        Subcategory entity = this.subcategoryMapper.subcategoryDTOToSubcategory(this.getSubcategoryById(id).getBody());

        if (entity.getStatus() == EntityStatus.DELETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La subcategoría no existe");
        }

        entity.delete();

        this.subcategoryRepository.save(entity);
    }
}
