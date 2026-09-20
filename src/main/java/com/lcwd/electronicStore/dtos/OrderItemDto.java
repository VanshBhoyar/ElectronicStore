package com.lcwd.electronicStore.dtos;

import com.lcwd.electronicStore.entities.Order;
import com.lcwd.electronicStore.entities.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private int orderItemId;

    private int quantity;

    private double totalPrice;

    private ProductDto product;

}
