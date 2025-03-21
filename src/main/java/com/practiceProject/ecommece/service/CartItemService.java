package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.CartItemException;
import com.practiceProject.ecommece.exception.UserException;
import com.practiceProject.ecommece.entity.Cart;
import com.practiceProject.ecommece.entity.CartItem;
import com.practiceProject.ecommece.entity.Product;


public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
