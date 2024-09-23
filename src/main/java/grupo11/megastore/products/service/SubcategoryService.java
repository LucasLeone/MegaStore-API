package grupo11.megastore.products.service;

import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.SubcategoryDTO;
import grupo11.megastore.products.mapper.SubcategoryMapper;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.repository.CategoryRepository;
import grupo11.megastore.products.repository.SubcategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubcategoryService implements ISubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
    @Transactional
    public Subcategory save(SubcategoryDTO subcategoryDTO) {
        // Verificar si el nombre ya existe
        if (isNameExists(subcategoryDTO.getName())) {
            throw new IllegalArgumentException("La subcategoría con el nombre '" + subcategoryDTO.getName() + "' ya existe.");
        }

        // Obtener la categoría asociada
        Category category = categoryRepository.findById(subcategoryDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + subcategoryDTO.getCategoryId() + " no encontrada."));

        // Mapear DTO a entidad
        Subcategory subcategory = SubcategoryMapper.toEntity(subcategoryDTO);
        subcategory.setCategory(category);
        subcategory.setStatus(Subcategory.ACTIVE);

        return subcategoryRepository.save(subcategory);
    }

    @Override
    @Transactional
    public Subcategory update(Integer id, SubcategoryDTO subcategoryDTO) {
        Subcategory existingSubcategory = getById(id);
        if (existingSubcategory == null) {
            throw new ResourceNotFoundException("Subcategoría con id " + id + " no encontrada.");
        }

        // Actualizar campos
        if (subcategoryDTO.getName() != null) {
            existingSubcategory.setName(subcategoryDTO.getName());
        }
        if (subcategoryDTO.getDescription() != null) {
            existingSubcategory.setDescription(subcategoryDTO.getDescription());
        }
        if (subcategoryDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(subcategoryDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + subcategoryDTO.getCategoryId() + " no encontrada."));
            existingSubcategory.setCategory(category);
        }

        return subcategoryRepository.save(existingSubcategory);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Subcategory subcategory = getById(id);
        if (subcategory == null) {
            throw new ResourceNotFoundException("Subcategoría con id " + id + " no encontrada.");
        }
        subcategory.markAsDeleted();
        subcategoryRepository.save(subcategory);
    }

    @Override
    public boolean isNameExists(String name) {
        return subcategoryRepository.findByName(name).isPresent();
    }
}
