package com.lcwd.electronicStore.services;

import com.lcwd.electronicStore.dtos.CategoryDto;
import com.lcwd.electronicStore.dtos.PageableResponse;

public interface CategoryService {
//    Create
    CategoryDto createCategory(CategoryDto categoryDto);

//    Update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

//    Delete
    void deleteCategory(String categoryId);

//    Get All
    PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

//    Get Single
    CategoryDto getSingleCategory(String categoryId);

}
