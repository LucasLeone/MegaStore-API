package grupo11.megastore.carts.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import grupo11.megastore.carts.dto.cart.CartDTO;
import grupo11.megastore.carts.model.Cart;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { CartItemMapper.class })
public interface CartMapper {

    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(target = "total", expression = "java(cart.getTotal())")
    CartDTO cartToCartDTO(Cart cart);
}
