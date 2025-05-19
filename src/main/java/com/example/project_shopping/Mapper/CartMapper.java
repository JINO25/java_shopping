package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Cart.CartDTO;
import com.example.project_shopping.DTO.Cart.CartItemDTO;
import com.example.project_shopping.DTO.Cart.ProductVariantCartDTO;
import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.CartItem;
import com.example.project_shopping.Entity.ProductVariant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartMapper {
    private ModelMapper modelMapper;

    public CartDTO toCartDTO(Cart cart){
        modelMapper.typeMap(Cart.class, CartDTO.class).addMappings(mapper->{
            mapper.map(src->src.getUser().getName(),CartDTO::setUserName);
        });
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setCartItems(toCartItemDTOList(new ArrayList<>(cart.getCartItems())));

        return cartDTO;
    }

    public List<CartDTO> toCartDTOList(List<Cart> cartList){
        return cartList.stream()
                .map(this::toCartDTO)
                .collect(Collectors.toList());
    }

    public List<CartItemDTO> toCartItemDTOList(List<CartItem> cartItems){
        return cartItems.stream()
                .map(this::toCartItemDTO)
                .collect(Collectors.toList());
    }

    public CartItemDTO toCartItemDTO(CartItem cartItem){
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());

        ProductVariant productVariant = cartItem.getProduct();
        ProductVariantCartDTO productVariantCartDTO = new ProductVariantCartDTO();
        productVariantCartDTO.setId(productVariant.getId());
        productVariantCartDTO.setProductName(productVariant.getProduct().getName());
        productVariantCartDTO.setOption(productVariant.getOption());
        productVariantCartDTO.setColor(productVariant.getColor());
        productVariantCartDTO.setPrice(productVariant.getPrice());

        cartItemDTO.setProductVariantCartDTO(productVariantCartDTO);
        return cartItemDTO;
    }
}
