package grupo11.megastore.products.controller;

import grupo11.megastore.products.dto.ProductDTO;
import grupo11.megastore.products.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Base64;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listAll() {
        List<ProductDTO> productsDTO = productService.listAll();
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Integer id) {
        ProductDTO productDTO = productService.getById(id);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO savedProductDTO = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updatePartial(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        ProductDTO existingProductDTO = productService.getById(id);

        if (productDTO.getName() != null) {
            existingProductDTO.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            existingProductDTO.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null) {
            existingProductDTO.setPrice(productDTO.getPrice());
        }
        if (productDTO.getStock() != null) {
            existingProductDTO.setStock(productDTO.getStock());
        }
        if (productDTO.getCategoryId() != null) {
            existingProductDTO.setCategoryId(productDTO.getCategoryId());
        }
        if (productDTO.getSubcategoryId() != null) {
            existingProductDTO.setSubcategoryId(productDTO.getSubcategoryId());
        }
        if (productDTO.getBrandId() != null) {
            existingProductDTO.setBrandId(productDTO.getBrandId());
        }
        if (productDTO.getStatus() != null) {
            existingProductDTO.setStatus(productDTO.getStatus());
        }
        if (productDTO.getImageBase64() != null) {
            existingProductDTO.setImageBase64(productDTO.getImageBase64());
        }

        ProductDTO updatedProductDTO = productService.update(existingProductDTO);
        return ResponseEntity.ok(updatedProductDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Integer id) {
        ProductDTO productDTO = productService.getById(id);
        if (productDTO.getImageBase64() == null || productDTO.getImageBase64().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        byte[] imageBytes = Base64.getDecoder().decode(productDTO.getImageBase64());

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(imageBytes);
    }
}
