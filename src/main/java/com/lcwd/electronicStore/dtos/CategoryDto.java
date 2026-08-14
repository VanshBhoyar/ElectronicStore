package com.lcwd.electronicStore.dtos;

import com.lcwd.electronicStore.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "Title is required!!")
    @Size(min = 4, message = "Category title requires more than 4 char !!")
    private String title;

    @NotBlank(message = "Description is required!!")
    private String description;
    @ImageNameValid
    private String coverImage;
}
