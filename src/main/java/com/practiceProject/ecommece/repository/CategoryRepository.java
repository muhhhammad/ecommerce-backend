package com.practiceProject.ecommece.repository;

import com.practiceProject.ecommece.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByName(String name);

    // The Query Does the Following Below:
    // Retrieve's a Category entity from the database
    // Filters the category by its name, matching it with the provided parameter (:name)
    // Further filters the category by checking if its parent category's name matches the provided parameter (:parentCategoryName)
    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.parentCategory.name = :parentCategoryName")
    public Category findByNameAndParent(@Param("name") String name, @Param("parentCategoryName") String parentCategoryName);


}
