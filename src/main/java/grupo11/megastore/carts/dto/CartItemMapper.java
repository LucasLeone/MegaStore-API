package grupo11.megastore.carts.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import grupo11.megastore.carts.dto.cartItems.CartItemDTO;
import grupo11.megastore.carts.model.CartItem;
import grupo11.megastore.products.dto.IVariantMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { IVariantMapper.class })
public interface CartItemMapper {
    
    @Mapping(source = "variant", target = "variant")
    CartItemDTO cartItemToCartItemDTO(CartItem cartItem);
    
    @Mapping(target = "cart", ignore = true)
    CartItem cartItemDTOToCartItem(CartItemDTO cartItemDTO);
}
