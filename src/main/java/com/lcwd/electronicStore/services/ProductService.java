package com.lcwd.electronicStore.services;

import com.lcwd.electronicStore.dtos.PageableResponse;
import com.lcwd.electronicStore.dtos.ProductDto;

public interface ProductService {

//    Create
    ProductDto createProduct(ProductDto productDto);
//    Update
    ProductDto updateProduct(ProductDto productDto, String productId);
//    Delete
    void deleteProduct(String productId);
//    Get Single
    ProductDto getSingleProduct(String productId);
    //    Get All
    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
//    Get All: Live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
//    Search By Title
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);


}
