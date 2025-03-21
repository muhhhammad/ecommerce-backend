package com.practiceProject.ecommece.controller;


import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.request.CreateProductRequest;
import com.practiceProject.ecommece.response.ApiResponse;
import com.practiceProject.ecommece.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
        productService.deleteProduct(productId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Product Deleted SuccessFully:");
        response.setStatus(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProduct(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> colors,
            @RequestParam(required = false) List<String> sizes,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        Page<Product> productPage = productService.getAllProduct(
                category, colors, sizes, pageNumber, minPrice, maxPrice, minDiscount, sort, stock, pageSize);
        List<Product> productList = productPage.getContent();  // Extract List<Product>
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }


    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product request,
                                                 @PathVariable Long productId) throws ProductException {

        Product product = productService.updateProduct(productId, request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProducts(@RequestBody CreateProductRequest[] requests) {

        for (CreateProductRequest product : requests) {
            productService.createProduct(product);
        }
        ApiResponse response = new ApiResponse();
        response.setMessage("Product Created Successfully");
        response.setStatus(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
