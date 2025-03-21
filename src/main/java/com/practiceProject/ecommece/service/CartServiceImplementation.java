package com.practiceProject.ecommece.service;


import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.entity.Cart;
import com.practiceProject.ecommece.entity.CartItem;
import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.entity.User;
import com.practiceProject.ecommece.repository.CartRepository;
import com.practiceProject.ecommece.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation implements CartService {

    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;

    @Autowired
    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    public Cart createCart(User user) {

        Cart cart = new Cart(); // Create a new cart instance
        cart.setUser(user); // Assign the cart to the user

        return cartRepository.save(cart); // Save and return the cart
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {

        Cart cart = cartRepository.findByUserId(userId); // Find cart by user ID
        Product product = productService.findProductById(req.getProductId()); // Find product by ID
        CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId); // Check if item exists in cart

        if(isPresent == null){ // If item is not present, create a new cart item

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product); // Set product
            cartItem.setCart(cart); // Link cart item to cart
            cartItem.setQuantity(req.getQuantity()); // Set quantity
            cartItem.setUserId(userId); // Assign user ID

            int totalPrice = req.getQuantity() * product.getDiscountedPrice(); // Calculate total price
            cartItem.setPrice(totalPrice);
            cartItem.setSize(req.getSize()); // Set size

            CartItem createdCartItem = cartItemService.createCartItem(cartItem); // Save cart item
            cart.getCartItems().add(createdCartItem); // Add cart item to cart

        }

        return "Item Added to Cart"; // Return success message
    }

    @Override
    public Cart findUserCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId); // Find cart by user ID

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for(CartItem cartItem : cart.getCartItems()){ // Loop through all cart items

            totalPrice = totalPrice + cartItem.getPrice(); // Calculate total price
            totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice(); // Calculate total discounted price
            totalItem = totalItem + cartItem.getQuantity(); // Count total quantity

        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice); // Set total discounted price
        cart.setTotalItem(totalItem); // Set total item count
        cart.setTotalPrice(totalPrice); // Set total price
        cart.setDiscount(totalPrice - totalDiscountedPrice); // Calculate discount

        return cartRepository.save(cart); // Save and return updated cart
    }

}
