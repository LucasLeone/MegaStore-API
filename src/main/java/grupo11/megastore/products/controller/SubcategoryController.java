package grupo11.megastore.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<SubcategoryDTO>> getAllSubcategories(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        List<SubcategoryDTO> subcategories = this.subcategoryService.getAllSubcategories(categoryId);
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<SubcategoryDTO>> getAllDeletedSubcategories() {
        List<SubcategoryDTO> subcategories = this.subcategoryService.getAllDeletedSubcategories();
        return new ResponseEntity<>(subcategories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(@PathVariable Long id) {
        SubcategoryDTO subcategory = this.subcategoryService.getSubcategoryById(id);
        return new ResponseEntity<>(subcategory, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SubcategoryDTO> createSubcategory(@Valid @RequestBody CreateSubcategoryDTO body) {
        SubcategoryDTO subcategory = this.subcategoryService.createSubcategory(body);
        return new ResponseEntity<>(subcategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> updateSubcategory(@PathVariable Long id, @Valid @RequestBody UpdateSubcategoryDTO body) {
        SubcategoryDTO subcategory = this.subcategoryService.updateSubcategory(id, body);
        return new ResponseEntity<>(subcategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        this.subcategoryService.deleteSubcategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
