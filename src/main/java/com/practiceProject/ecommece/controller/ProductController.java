package com.practiceProject.ecommece.controller;


import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    //Will return all the products on "/product" endpoint
    @GetMapping("/product")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category, @RequestParam List<String> color,
                                                                      @RequestParam List<String> size, @RequestParam Integer pageNumber,
                                                                      @RequestParam Integer minPrice, @RequestParam Integer maxPrice,
                                                                      @RequestParam Integer minDiscount, @RequestParam String sort,
                                                                      @RequestParam String stock, @RequestParam Integer pageSize) {

        Page<Product> res = productService.getAllProduct(category, color, size, pageNumber, minPrice, maxPrice, minDiscount, sort, stock, pageSize);

        System.out.println("Complete Products...");

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);

    }

    //Will return a product searched by ID
    @GetMapping("/product/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {


        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);


    }


    //Will return a product searched by Name
//    @GetMapping("/product/search")
//    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String productToSearch){
//
//
//        List<Product> product = productService.searchProduct(productToSearch);
//        return new ResponseEntity<List<Product>>(product, HttpStatus.OK);
//
//
//    }


}
