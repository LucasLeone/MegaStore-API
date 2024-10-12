package grupo11.megastore.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        List<VariantDTO> variants = this.variantService.getAllVariants(productId, color, size);
        return new ResponseEntity<>(variants, HttpStatus.OK);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<VariantDTO>> getAllDeletedVariants() {
        List<VariantDTO> variants = this.variantService.getAllDeletedVariants();
        return new ResponseEntity<>(variants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantDTO> getVariantById(@PathVariable Long id) {
        VariantDTO variant = this.variantService.getVariantById(id);
        return new ResponseEntity<>(variant, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VariantDTO> createVariant(@Valid @RequestBody CreateVariantDTO body) {
        VariantDTO variant = this.variantService.createVariant(body);
        return new ResponseEntity<>(variant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantDTO> updateVariant(@PathVariable Long id, @Valid @RequestBody UpdateVariantDTO body) {
        VariantDTO variant = this.variantService.updateVariant(id, body);
        return new ResponseEntity<>(variant, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariant(@PathVariable Long id) {
        this.variantService.deleteVariant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreVariant(@PathVariable Long id) {
        this.variantService.restoreVariant(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
