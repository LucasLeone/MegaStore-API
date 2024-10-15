package grupo11.megastore.carts.controller;

import grupo11.megastore.carts.dto.cart.CartDTO;
import grupo11.megastore.carts.interfaces.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping("/items/{variantId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long variantId, @PathVariable Integer quantity) {
        CartDTO cart = this.cartService.addProductToCart(variantId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<CartDTO> getMyCart() {
        CartDTO cart = this.cartService.getMyCart();
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/items/{variantId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateProductQuantity(@PathVariable Long variantId, @PathVariable Integer quantity) {
        CartDTO cart = this.cartService.updateProductQuantityInCart(variantId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/items/{variantId}")
    public ResponseEntity<Void> deleteProductFromCart(@PathVariable Long variantId) {
        this.cartService.deleteProductFromCart(variantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}