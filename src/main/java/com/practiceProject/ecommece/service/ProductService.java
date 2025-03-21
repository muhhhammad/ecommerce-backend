package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest request);

    public String deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product req) throws ProductException;

    public Product findProductById(Long id) throws ProductException;

    public List<Product> findProductByCategory(String category);

    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer pageNumber,
                                       Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageSize);

}
