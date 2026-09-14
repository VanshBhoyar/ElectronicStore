package com.lcwd.electronicStore.services.Impl;

import com.lcwd.electronicStore.dtos.AddItemToCartRequest;
import com.lcwd.electronicStore.dtos.CartDto;
import com.lcwd.electronicStore.entities.Cart;
import com.lcwd.electronicStore.entities.CartItem;
import com.lcwd.electronicStore.entities.Product;
import com.lcwd.electronicStore.entities.User;
import com.lcwd.electronicStore.exceptions.BadApiRequestException;
import com.lcwd.electronicStore.exceptions.ResourceNotFountException;
import com.lcwd.electronicStore.repositories.CartItemRepository;
import com.lcwd.electronicStore.repositories.CartRepository;
import com.lcwd.electronicStore.repositories.ProductRepository;
import com.lcwd.electronicStore.repositories.UserRepository;
import com.lcwd.electronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if(quantity<=0){
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFountException("Product not found in Database!!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFountException("User not found in Database!!"));

        Cart cart = null;

        try {
            cart=cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        List<CartItem> updatedItems = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);

        if(!updated.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFountException("CartItem not found !!"));
        cartItemRepository.delete(cartItem1);

    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFountException("User not found with given Id!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFountException("Cart not found !!"));
        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFountException("User not found with given Id!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFountException("Cart not found !!"));
        return mapper.map(cart, CartDto.class);
    }
}
