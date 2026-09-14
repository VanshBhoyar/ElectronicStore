package com.lcwd.electronicStore.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemToCartRequest {
    private String productId;
    private int quantity;
}
