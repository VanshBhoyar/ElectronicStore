package com.lcwd.electronicStore.controllers;

import com.lcwd.electronicStore.dtos.*;
import com.lcwd.electronicStore.services.CategoryService;
import com.lcwd.electronicStore.services.FileService;
import com.lcwd.electronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable String categoryId
    ){
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Category deleted successfully!!").success(true).status(HttpStatus.OK).build();
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<CategoryDto> allCategory = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allCategory,HttpStatus.OK);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId){
        CategoryDto singleCategory = categoryService.getSingleCategory(categoryId);
        return new ResponseEntity<>(singleCategory,HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable("categoryId") String categoryId
    ) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);

        CategoryDto category = categoryService.getSingleCategory(categoryId);
        category.setCoverImage(imageName);
        CategoryDto categoryDto = categoryService.updateCategory(category, categoryId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("Image uploaded successfully")
                .status(HttpStatus.CREATED)
                .success(true)
                .build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public void serverCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

        CategoryDto category = categoryService.getSingleCategory(categoryId);
        logger.info("ImageName : {}",category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto productDto
    ){
        ProductDto createWithCategory = productService.createWithCategory(categoryId, productDto);
        return new ResponseEntity<>(createWithCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategory(
            @PathVariable String categoryId,
            @PathVariable String productId
    ){
        ProductDto productDto = productService.updateCategoryWithProduct(categoryId, productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllCategoryProducts(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> allCategoryProducts = productService.getAllCategoryProducts(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allCategoryProducts, HttpStatus.OK);
    }
}
