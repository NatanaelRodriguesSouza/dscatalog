package com.example.DsCatalog.services;

import com.example.DsCatalog.Dto.CategoryDTO;
import com.example.DsCatalog.entities.Category;
import com.example.DsCatalog.repository.CategoryRepository;
import com.example.DsCatalog.services.excepetions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Transactional
    public List<CategoryDTO> findAll(){
        List<Category> result = categoryRepository.findAll();
        List<CategoryDTO> list = result.stream().map(x->new CategoryDTO(x)).collect(Collectors.toList());
        return list;
    }
    @Transactional
    public CategoryDTO findById(Long id){
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow(()-> new EntityNotFoundException("Entidade não encontrada"));
        return new CategoryDTO(entity);
    }
    @Transactional
    public CategoryDTO insert(CategoryDTO dto){
        Category category = new Category();
        category.setName(dto.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category);
    }
}
