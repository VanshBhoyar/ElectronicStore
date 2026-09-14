package com.lcwd.electronicStore.services;

import com.lcwd.electronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronicStore.dtos.CartDto;

public interface CartService {
//    To add items in cart
//    Case1: cart available : add items to cart
//    Case2: cart unavailable : create new cart and add items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

//    Remove Item From Cart
    void removeItemFromCart(String userId,int cartItem);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
