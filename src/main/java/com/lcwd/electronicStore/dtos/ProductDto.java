package com.lcwd.electronicStore.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String productId;
    @NotBlank(message = "Title is required!!")
    @Size(min = 4, message = "Product title is more than 4 char!!")
    private String title;
    @NotBlank(message = "Description is required!!")
    private String description;
    private int quantity;
    private double price;
    private double discountedPrice;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String ImageName;
    private CategoryDto category;
}
