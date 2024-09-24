package grupo11.megastore.products.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import grupo11.megastore.products.interfaces.IBrandService;
import jakarta.validation.Valid;
import grupo11.megastore.products.dto.brand.BrandDTO;
import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import grupo11.megastore.products.dto.brand.UpdateBrandDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/brands")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        return this.brandService.getAllBrands();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        return this.brandService.getBrandById(id);
    }

    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@Valid @RequestBody CreateBrandDTO body) {
        return this.brandService.createBrand(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable Long id, @Valid @RequestBody UpdateBrandDTO body) {
        return this.brandService.updateBrand(id, body);
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) {
        this.brandService.deleteBrand(id);
    }
}
