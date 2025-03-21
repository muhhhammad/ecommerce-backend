package com.practiceProject.ecommece.controller;

import com.practiceProject.ecommece.entity.Cart;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.request.AddItemRequest;
import com.practiceProject.ecommece.response.ApiResponse;
import com.practiceProject.ecommece.service.CartService;
import com.practiceProject.ecommece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
//@Tag(name = "Cart Management", description = "Find User Cart, Add Item to Cart") // This Annotation is for Documentation And It is Completely Optional
public class CartController {

    private CartService cartService;
    private UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/")
    //@Operation(description = "Find Cart By User ID") //This Annotation is for Documentation And It is Completely Optional
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {

        //Finding user through JSON Web Token
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getUserId());

        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PutMapping("/add")
    //@Operation(description = "Add item to Cart) //This Annotation is for Documentation And It is Completely Optional
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest request,
                                                     @RequestHeader("Authorization") String jwt) throws UserException, ProductException {

        //Finding user through JSON Web Token
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(user.getUserId(), request);

        ApiResponse response = new ApiResponse();
        response.setMessage("Item Added to Cart Successfully");
        response.setStatus(true);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
