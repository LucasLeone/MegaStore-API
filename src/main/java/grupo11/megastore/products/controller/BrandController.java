package grupo11.megastore.products.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<BrandDTO> brands = this.brandService.getAllBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<BrandDTO>> getAllDeletedBrands() {
        List<BrandDTO> brands = this.brandService.getAllDeletedBrands();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable Long id) {
        BrandDTO dto = this.brandService.getBrandById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@Valid @RequestBody CreateBrandDTO body) {
        BrandDTO dto = this.brandService.createBrand(body);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable Long id, @Valid @RequestBody UpdateBrandDTO body) {
        BrandDTO dto = this.brandService.updateBrand(id, body);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        this.brandService.deleteBrand(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreBrand(@PathVariable Long id) {
        this.brandService.restoreBrand(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
