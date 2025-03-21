package com.practiceProject.ecommece.service;

import com.practiceProject.ecommece.exception.ProductException;
import com.practiceProject.ecommece.entity.Category;
import com.practiceProject.ecommece.entity.Product;
import com.practiceProject.ecommece.repository.CategoryRepository;
import com.practiceProject.ecommece.repository.ProductRepository;
import com.practiceProject.ecommece.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;


@Service
public class ProductServiceImplementation implements ProductService{


    private ProductRepository productRepository;
    private UserService userService;
    private CategoryRepository categoryRepository;


    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository,
                                        UserService userService,
                                        CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(CreateProductRequest request) {

        // Retrieve the top-level category by name
        Category topLevel = categoryRepository.findByName(request.getTopLevelCategory());
        if(topLevel == null){ // If it doesn't exist, create and save it

            Category topLevelCategory = new Category();
            topLevelCategory.setName(request.getTopLevelCategory());
            topLevelCategory.setLevel(1); // Assign level 1 to the top category
            topLevel = categoryRepository.save(topLevelCategory);

        }

        // Retrieve the second-level category by name and its parent
        Category secondLevel = categoryRepository.findByNameAndParent(request.getSecondLevelCategory(), topLevel.getName());
        if(secondLevel == null){ // If it doesn't exist, create and save it

            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(request.getSecondLevelCategory());
            secondLevelCategory.setLevel(2); // Assign level 2 to the second category
            secondLevel = categoryRepository.save(secondLevelCategory);

        }

        // Retrieve the third-level category by name and its parent
        Category thirdLevel = categoryRepository.findByNameAndParent(request.getSecondLevelCategory(), secondLevel.getName());
        if(thirdLevel == null){ // If it doesn't exist, create and save it

            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(request.getSecondLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel); // Set its parent as second-level category
            thirdLevelCategory.setLevel(3); // Assign level 3 to the third category
            thirdLevel = categoryRepository.save(thirdLevelCategory);

        }

        // Create a new product and set its properties from the request
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setDescription(request.getDescription());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setDiscountPersent(request.getDiscountPercent());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setSizes(request.getSize());
        product.setQuantity(request.getQuantity());
        product.setCategory(thirdLevel); // Assign the product to the third-level category
        product.setCreatedAt(LocalDate.from(LocalDateTime.now())); // Set the creation date

        // Save the product in the database
        Product savedProduct = productRepository.save(product);

        return savedProduct; // Return the saved product
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {

        Product product = findProductById(productId); // Find the product by ID, throws exception if not found
        product.getSizes().clear(); // Clear size associations before deletion
        productRepository.delete(product); // Delete the product from the repository

        return "Product Deleted Successfully"; // Return success message
    }


    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {

        Product product = findProductById(productId); // Find the product by its ID, throws exception if not found

        if(req.getQuantity() != 0){ // Check if the requested quantity is not zero
            product.setQuantity(req.getQuantity()); // Update the product's quantity
        }

        return productRepository.save(product); // Save and return the updated product
    }


    @Override
    public Product findProductById(Long id) throws ProductException {

        Optional<Product> opt = productRepository.findById(id); // Retrieve the product by ID from the repository

        if(opt.isPresent()){ // Check if the product exists
            return opt.get(); // Return the found product
        }
        throw new ProductException("Product Not Found With Given ID: " + id); // Throw exception if product is not found

    }


    @Override
    public List<Product> findProductByCategory(String category) {
        return of();
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer pageNumber,
                                       Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize); // Create a pageable object for pagination
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort); // Fetch filtered products

        if(!colors.isEmpty()){ // Apply color filtering if colors are provided

            products = products.stream() // Convert the products list into a stream
                    .filter(p -> colors.stream() // Filter products based on color
                            .anyMatch(c -> c.equalsIgnoreCase(p.getColor()))) // Check if any color in the list matches the product's color (case insensitive)
                    .toList(); // Convert the filtered stream back into a list


        }

        if(stock != null){ // Apply stock availability filter
            if(stock.equals("in_stock")){
                products = products.stream().filter(p -> p.getQuantity() > 0).toList(); // Include only in-stock products

            } else if(stock.equals("Out_of_Stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).toList(); // Include only out-of-stock products

            }

        }

        int startIndex = (int) pageable.getOffset(); // Calculate the start index for pagination
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size()); // Calculate the end index based on available products
        List<Product> pageContent = products.subList(startIndex, endIndex); // Extract paginated content

        Page<Product> filteredProduct = new PageImpl<>(pageContent, pageable, products.size()); // Create a paginated response

        return filteredProduct; // Return the final paginated product list
    }

}
