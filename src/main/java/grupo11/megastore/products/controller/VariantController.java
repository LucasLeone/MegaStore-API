package grupo11.megastore.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import grupo11.megastore.products.dto.variant.CreateVariantDTO;
import grupo11.megastore.products.dto.variant.UpdateVariantDTO;
import grupo11.megastore.products.dto.variant.VariantDTO;
import grupo11.megastore.products.interfaces.IVariantService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/variants")
public class VariantController {
    
    @Autowired
    private IVariantService variantService;

    @GetMapping
    public ResponseEntity<List<VariantDTO>> getAllVariants(
        @RequestParam(required = false) Long productId,
        @RequestParam(required = false) String color,
        @RequestParam(required = false) String size
    ) {
        return this.variantService.getAllVariants(productId, color, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantDTO> getVariantById(@PathVariable Long id) {
        return this.variantService.getVariantById(id);
    }

    @PostMapping
    public ResponseEntity<VariantDTO> createVariant(@Valid @RequestBody CreateVariantDTO body) {
        return this.variantService.createVariant(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantDTO> updateVariant(@PathVariable Long id, @Valid @RequestBody UpdateVariantDTO body) {
        return this.variantService.updateVariant(id, body);
    }

    @DeleteMapping("/{id}")
    public void deleteVariant(@PathVariable Long id) {
        this.variantService.deleteVariant(id);
    }
}
