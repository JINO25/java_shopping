package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Cart.CartDTO;
import com.example.project_shopping.DTO.Cart.CartReqDTO;
import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.CartItem;
import com.example.project_shopping.Entity.ProductVariant;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Enums.CartStatus;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Exception.InvalidTokenException;
import com.example.project_shopping.Mapper.CartMapper;
import com.example.project_shopping.Repository.CartItemRepository;
import com.example.project_shopping.Repository.CartRepository;
import com.example.project_shopping.Repository.ProductVariantRepository;
import com.example.project_shopping.Repository.UserRepository;
import com.example.project_shopping.Service.CartService;
import com.example.project_shopping.Util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImp implements CartService {
    private CartMapper cartMapper;
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ProductVariantRepository productVariantRepository;
    private CartItemRepository cartItemRepository;

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    public List<CartDTO> getCartForCurrentUser() {
        Integer userId = Auth.getCurrentUserID();
        List<Cart> cartList = cartRepository.findByUserId(userId);
        if (cartList.isEmpty()) {
            return Collections.emptyList();
        }
        return cartMapper.toCartDTOList(cartList);
    }

    @Override
    public CartDTO addItem(CartReqDTO cartReqDTO) {
        Integer userID = Auth.getCurrentUserID();
        if (userID == null) {
            throw new EntityNotFoundException("User not found!");
        }
        User user = userRepository.findById(userID).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userID));
        Cart cart = cartRepository.findByUserId(userID)
                .stream().findFirst().orElseGet(() -> createCart(user));

        Optional<CartItem> existingItemOpt = cart.getCartItems()
                .stream().filter(item -> item.getProduct().getId().equals(cartReqDTO.getProductVariantId())).findFirst();

        if (existingItemOpt.isPresent() && !existingItemOpt.get().getStatus().equals(CartStatus.CHECKD_OUT)) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartReqDTO.getQuantity());
        } else {
            ProductVariant productVariant = productVariantRepository.findById(cartReqDTO.getProductVariantId())
                    .orElseThrow(() -> new EntityNotFoundException("Product variant not found!"));

            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setQuantity(cartReqDTO.getQuantity());
            newItem.setProduct(productVariant);
            cart.getCartItems().add(newItem);
        }

        cart = cartRepository.save(cart);

        return cartMapper.toCartDTO(cart);
    }

    @Override
    public CartDTO updateItemQuantity(Integer cartItemId, Integer newQuantity) {
        if (newQuantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        cartItem.setQuantity(newQuantity);

        cartItemRepository.save(cartItem);

        Cart cart = cartItem.getCart();

        return cartMapper.toCartDTO(cart);
    }

    @Override
    public void removeItem(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        Cart cart = cartItem.getCart();

        cart.getCartItems().remove(cartItem);

        cartItemRepository.delete(cartItem);

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCartItem() {
        Integer userId = Auth.getCurrentUserID();
        if(userId == null){
            throw new EntityNotFoundException("User not login!");
        }

        User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found!"));

        Cart cart = cartRepository.findCartByUser(user);

        if(cart == null){
            throw new EntityNotFoundException("Cart not found!");
        }

        cartItemRepository.cleanItem(cart.getId(), CartStatus.PENDING);
    }
}
