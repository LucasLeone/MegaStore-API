package grupo11.megastore.products.controller;

import java.util.List;
import java.util.stream.Collectors;

import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.BrandDTO;
import grupo11.megastore.products.mapper.BrandMapper;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.service.IBrandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brands")
public class BrandController {
    
    @Autowired
    private IBrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> listAll() {
        List<Brand> brands = brandService.listAll();
        List<BrandDTO> brandsDTO = brands.stream()
                .map(BrandMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(brandsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getById(@PathVariable Integer id) {
        Brand brand = brandService.getById(id);

        if (brand == null) {
            throw new ResourceNotFoundException("Brand with id " + id + " not found.");
        }

        BrandDTO brandDTO = BrandMapper.toDTO(brand);
        return ResponseEntity.ok(brandDTO);
    }

    @PostMapping
    public ResponseEntity<BrandDTO> save(@RequestBody BrandDTO brandDTO) {
        Brand brand = BrandMapper.toEntity(brandDTO);
        Brand savedBrand = brandService.save(brand);
        BrandDTO savedBrandDTO = BrandMapper.toDTO(savedBrand);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBrandDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BrandDTO> updatePartial(@PathVariable Integer id, @RequestBody BrandDTO brandDTO) {
        Brand existingBrand = brandService.getById(id);
        if (existingBrand == null) {
            throw new ResourceNotFoundException("Brand with id " + id + " not found.");
        }

        if (brandDTO.getName() != null) {
            existingBrand.setName(brandDTO.getName());
        }

        if (brandDTO.getDescription() != null) {
            existingBrand.setDescription(brandDTO.getDescription());
        }

        Brand updatedBrand = brandService.update(existingBrand);
        BrandDTO updatedBrandDTO = BrandMapper.toDTO(updatedBrand);
        return ResponseEntity.ok(updatedBrandDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Brand brand = brandService.getById(id);
        if (brand == null) {
            throw new ResourceNotFoundException("Brand with id " + id + " not found.");
        }

        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
