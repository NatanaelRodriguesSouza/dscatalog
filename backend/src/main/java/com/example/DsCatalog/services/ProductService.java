package com.example.DsCatalog.services;

import com.example.DsCatalog.Dto.CategoryDTO;
import com.example.DsCatalog.Dto.ProductDTO;
import com.example.DsCatalog.entities.Category;
import com.example.DsCatalog.entities.Product;
import com.example.DsCatalog.repository.CategoryRepository;
import com.example.DsCatalog.repository.ProductRepository;
import com.example.DsCatalog.services.excepetions.DatabaseException;
import com.example.DsCatalog.services.excepetions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Transactional
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> result = repository.findAll(pageRequest);
        Page<ProductDTO> list = result.map(x->new ProductDTO(x));
        return list;
    }
    @Transactional
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entidade não encontrada"));
        return new ProductDTO(entity,entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
        copyToDtoToEntity(dto,entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            copyToDtoToEntity(dto,entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        }
        catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyToDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
