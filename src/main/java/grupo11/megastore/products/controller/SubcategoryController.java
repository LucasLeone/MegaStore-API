package grupo11.megastore.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import grupo11.megastore.products.dto.subcategory.SubcategoryDTO;
import grupo11.megastore.products.dto.subcategory.UpdateSubcategoryDTO;
import grupo11.megastore.products.dto.subcategory.CreateSubcategoryDTO;
import grupo11.megastore.products.interfaces.ISubcategoryService;

@RestController
@RequestMapping(path = "/subcategories")
public class SubcategoryController {
    
    @Autowired
    private ISubcategoryService subcategoryService;

    @GetMapping
    public ResponseEntity<List<SubcategoryDTO>> getAllSubcategories() {
        return this.subcategoryService.getAllSubcategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(@PathVariable Long id) {
        return this.subcategoryService.getSubcategoryById(id);
    }

    @PostMapping
    public ResponseEntity<SubcategoryDTO> createSubcategory(@Valid @RequestBody CreateSubcategoryDTO body) {
        return this.subcategoryService.createSubcategory(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> updateSubcategory(@PathVariable Long id, @Valid @RequestBody UpdateSubcategoryDTO body) {
        return this.subcategoryService.updateSubcategory(id, body);
    }

    @DeleteMapping("/{id}")
    public void deleteSubcategory(@PathVariable Long id) {
        this.subcategoryService.deleteSubcategory(id);
    }
}
