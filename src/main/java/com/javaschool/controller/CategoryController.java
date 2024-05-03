package com.javaschool.controller;

import com.javaschool.entity.Category;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.repository.course.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id.toString()));

        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/")
    public ResponseEntity<Category> getCategoryByName(@RequestParam String name){
        Category category = categoryRepository.findByName(name);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategory(){
        return ResponseEntity.ok().body(categoryRepository.findAll());
    }


    @PostMapping("/")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){

        return ResponseEntity.ok().body(categoryRepository.save(category));
    }


}
