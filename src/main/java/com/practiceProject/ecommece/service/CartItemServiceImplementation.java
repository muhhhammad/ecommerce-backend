package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.CartItemException;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.entity.Cart;
import com.practiceProject.ecommece.entity.CartItem;
import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.repository.CartItemRepository;
import com.practiceProject.ecommece.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImplementation implements CartItemService{

    private CartItemRepository cartItemRepository;
    private UserService userService;
    private CartRepository cartRepository;

    @Autowired
    public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {

        cartItem.setQuantity(1); // Set initial quantity to 1
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity()); // Calculate total price
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity()); // Calculate total discounted price

        CartItem createdCartitem = cartItemRepository.save(cartItem); // Save cart item to database

        return createdCartitem; // Return saved cart item
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {

        CartItem item = findCartItemById(id); // Find cart item by ID
        User user = userService.findUserById(item.getUserId()); // Find user associated with cart item

        if(user.getUserId().equals(userId)){ // Check if the requesting user owns the cart item

            item.setQuantity(cartItem.getQuantity()); // Update quantity
            item.setPrice(item.getQuantity() * item.getProduct().getPrice()); // Update total price
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity()); // Update total discounted price

        }

        return cartItemRepository.save(item); // Save updated cart item
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {

        return cartItemRepository.isCartItemExist(cart, product, size, userId); // Check if cart item exists in database
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {

        CartItem cartItem = findCartItemById(cartItemId); // Find cart item by ID
        User user = userService.findUserById(cartItem.getUserId()); // Get owner of cart item
        User reqUser = userService.findUserById(userId); // Get requesting user

        if(user.getUserId().equals(reqUser.getUserId())){ // Ensure user owns the cart item

            cartItemRepository.deleteById(cartItemId); // Delete cart item from database

        } else {

            throw new UserException("Can't Remove Item"); // Throw exception if user is unauthorized

        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {

        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId); // Find cart item by ID
        if(optionalCartItem.isPresent()){

            return optionalCartItem.get(); // Return cart item if found

        }
        throw new CartItemException("Cart Item Not Found With ID: " + cartItemId); // Throw exception if not found
    }

}
