package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.exception.CartItemException;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.entity.CartItem;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.response.ApiResponse;
import com.practiceProject.ecommece.service.CartItemService;
import com.practiceProject.ecommece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    private CartItemService cartItemService;
    private UserService userService;

    @Autowired
    public CartItemController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, CartItemException{

        // Retrieve the user profile using the JWT token
        User user = userService.findUserProfileByJwt(jwt);

        // Remove the cart item for the authenticated user
        cartItemService.removeCartItem(user.getUserId(), cartItemId);

        // Create response object with success message
        ApiResponse response = new ApiResponse();
        response.setMessage("Item Deleted From Cart");
        response.setStatus(true);

        // Return the response entity with HTTP status 200 (OK)
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem,
                                                   @PathVariable Long cartItemId,
                                                   @RequestHeader("Authorization") String jwt) throws UserException, CartItemException{

        // Retrieve the user profile using the JWT token
        User user = userService.findUserProfileByJwt(jwt);

        // Update the cart item for the authenticated user
        CartItem updatedCartItem = cartItemService.updateCartItem(user.getUserId(), cartItemId, cartItem);

        // Return the updated cart item with HTTP status 200 (OK)
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }



}


//The Code Written By Muhammad Misbah.....