package grupo11.megastore.carts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupo11.megastore.carts.dto.cart.CartDTO;
import grupo11.megastore.carts.dto.cartItems.CartItemDTO;
import grupo11.megastore.carts.interfaces.ICartService;
import grupo11.megastore.carts.dto.CartMapper;
import grupo11.megastore.carts.dto.CartItemMapper;
import grupo11.megastore.carts.model.Cart;
import grupo11.megastore.carts.model.CartItem;
import grupo11.megastore.carts.model.repository.CartItemRepository;
import grupo11.megastore.carts.model.repository.CartRepository;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.model.repository.VariantRepository;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartMapper modelMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public CartDTO addProductToCart(Long variantId, Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        Cart cart = user.getCart();

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cartRepository.save(cart);
        }

        if (quantity == null || quantity <= 0) {
            throw new APIException("La cantidad debe ser un número positivo.");
        }

        Variant variant = variantRepository.findByIdAndStatus(variantId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Variante", "variantId", variantId));

        Product product = variant.getProduct();

        boolean itemExists = cartItemRepository.findByCartCartIdAndVariantId(cart.getCartId(), variantId).isPresent();

        if (itemExists) {
            throw new APIException("La variante " + variant.getId() + " del producto " + variant.getProduct().getName() + " ya existe en el carrito");
        }

        if (variant.getStock() == 0) {
            throw new APIException("La variante " + variant.getId() + " no está disponible");
        }

        if (variant.getStock() < quantity) {
            throw new APIException("Por favor, realiza un pedido de la variante " + variant.getId()
                    + " menor o igual a la cantidad " + variant.getStock() + ".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setVariant(variant);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getPrice());

        cart.getCartItems().add(newCartItem);

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.cartToCartDTO(cart);

        List<CartItemDTO> cartItems = cart.getCartItems().stream()
                .map(cartItemMapper::cartItemToCartItemDTO)
                .collect(Collectors.toList());

        cartDTO.setCartItems(cartItems);

        return cartDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO getMyCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Carrito", "usuario", email);
        }

        CartDTO cartDTO = modelMapper.cartToCartDTO(cart);
        List<CartItemDTO> cartItems = cart.getCartItems().stream()
                .map(cartItemMapper::cartItemToCartItemDTO)
                .collect(Collectors.toList());
        cartDTO.setCartItems(cartItems);

        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long variantId, Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Carrito", "usuario", email);
        }

        if (quantity == null || quantity <= 0) {
            throw new APIException("La cantidad debe ser un número positivo.");
        }

        Variant variant = variantRepository.findByIdAndStatus(variantId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Variante", "variantId", variantId));

        if (variant.getStock() == 0) {
            throw new APIException("La variante " + variant.getId() + " no está disponible");
        }

        if (variant.getStock() < quantity) {
            throw new APIException("Por favor, realiza un pedido de la variante con id " + variant.getId()
                    + " menor o igual a la cantidad " + variant.getStock() + ".");
        }

        CartItem cartItem = cartItemRepository.findByCartCartIdAndVariantId(cart.getCartId(), variantId)
                .orElseThrow(() -> new APIException("La variante " + variant.getId() + " no está en el carrito"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        CartDTO cartDTO = modelMapper.cartToCartDTO(cart);
        List<CartItemDTO> cartItems = cart.getCartItems().stream()
                .map(cartItemMapper::cartItemToCartItemDTO)
                .collect(Collectors.toList());
        cartDTO.setCartItems(cartItems);

        return cartDTO;
    }

    @Override
    @Transactional
    public void deleteProductFromCart(Long variantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Carrito", "usuario", email);
        }

        CartItem cartItem = cartItemRepository.findByCartCartIdAndVariantId(cart.getCartId(), variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id de la variante", variantId));

        cartItemRepository.delete(cartItem);
    }
}
