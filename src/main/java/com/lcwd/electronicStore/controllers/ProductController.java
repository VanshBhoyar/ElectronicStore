package com.lcwd.electronicStore.controllers;

import com.lcwd.electronicStore.dtos.*;
import com.lcwd.electronicStore.services.FileService;
import com.lcwd.electronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto){

        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@Valid @RequestBody ProductDto productDto, @PathVariable String productId){
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId){
        productService.deleteProduct(productId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Product deleted successfully!!").success(true).status(HttpStatus.OK).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId){
        ProductDto singleProduct = productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allLive = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allLive, HttpStatus.OK);
    }

    @GetMapping("/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchByTitle(
            @PathVariable String subTitle,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> response = productService.searchByTitle(subTitle, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    Upload Image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage")MultipartFile image
            ) throws IOException {
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.getSingleProduct(productId);
        productDto.setImageName(fileName);
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getImageName()).message("Product image is successfully uploaded !!").status(HttpStatus.CREATED).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    Serve Image
@GetMapping("/image/{productId}")
public void serverProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

    ProductDto productDto = productService.getSingleProduct(productId);
    InputStream resource = fileService.getResource(imagePath, productDto.getImageName());
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    StreamUtils.copy(resource,response.getOutputStream());

}

}
