package com.practiceProject.ecommece.repository;

import com.practiceProject.ecommece.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    //The Query performs the things below:
    // Retrieves all products from the Product entity
    // Filters products by category if a category is provided, otherwise, no category filter is applied
    // Filters products with a minimum price if specified
    // Filters products with a maximum price if specified
    // Filters products based on the minimum discount percentage if provided
    // Orders the result set based on sorting criteria
    // If sorting is set to 'price_low', sorts products by discounted price in ascending order
    // If sorting is set to 'price_high', sorts products by discounted price in descending order
    @Query("SELECT p FROM Product p " +
            "WHERE (:category IS NULL OR p.category.name = :category) " +
            "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
            "AND (:minDiscount IS NULL OR p.discountPersent >= :minDiscount) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
            "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC")
    public List<Product> filterProducts(@Param("category") String category,
                                        @Param("minPrice") Integer minPrice,
                                        @Param("maxPrice") Integer maxPrice,
                                        @Param("minDiscount") Integer minDiscount,
                                        @Param("sort") String sort);

}
