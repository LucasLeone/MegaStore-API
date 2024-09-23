package grupo11.megastore.products.controller;

import java.util.List;
import java.util.stream.Collectors;

import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.SubcategoryDTO;
import grupo11.megastore.products.mapper.SubcategoryMapper;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.service.ISubcategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subcategories")
public class SubcategoryController {
    
    @Autowired
    private ISubcategoryService subcategoryService;

    @GetMapping
    public ResponseEntity<List<SubcategoryDTO>> listAll() {
        List<Subcategory> subcategories = subcategoryService.listAll();
        List<SubcategoryDTO> subcategoriesDTO = subcategories.stream()
                .map(SubcategoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subcategoriesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> getById(@PathVariable Integer id) {
        Subcategory subcategory = subcategoryService.getById(id);

        if (subcategory == null) {
            throw new ResourceNotFoundException("Subcategoría con id " + id + " no encontrada.");
        }

        SubcategoryDTO subcategoryDTO = SubcategoryMapper.toDTO(subcategory);
        return ResponseEntity.ok(subcategoryDTO);
    }

    @PostMapping
    public ResponseEntity<SubcategoryDTO> save(@Valid @RequestBody SubcategoryDTO subcategoryDTO) {
        Subcategory savedSubcategory = subcategoryService.save(subcategoryDTO);
        SubcategoryDTO savedSubcategoryDTO = SubcategoryMapper.toDTO(savedSubcategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubcategoryDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> updatePartial(@PathVariable Integer id, @RequestBody SubcategoryDTO subcategoryDTO) {
        Subcategory updatedSubcategory = subcategoryService.update(id, subcategoryDTO);
        SubcategoryDTO updatedSubcategoryDTO = SubcategoryMapper.toDTO(updatedSubcategory);
        return ResponseEntity.ok(updatedSubcategoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Subcategory subcategory = subcategoryService.getById(id);
        if (subcategory == null) {
            throw new ResourceNotFoundException("Subcategoría con id " + id + " no encontrada.");
        }

        subcategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
