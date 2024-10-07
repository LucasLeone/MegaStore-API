package grupo11.megastore.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo11.megastore.products.dto.product.CreateProductDTO;
import grupo11.megastore.products.dto.product.ProductDTO;
import grupo11.megastore.products.dto.product.UpdateProductDTO;
import grupo11.megastore.products.interfaces.IProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String name
    ) {
        List<ProductDTO> products = this.productService.getAllProducts(categoryId, subcategoryId, brandId, name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<ProductDTO>> getAllDeletedProducts() {
        List<ProductDTO> products = this.productService.getAllDeletedProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = this.productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductDTO body) {
        ProductDTO product = this.productService.createProduct(body);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductDTO body) {
        ProductDTO product = this.productService.updateProduct(id, body);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        this.productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
