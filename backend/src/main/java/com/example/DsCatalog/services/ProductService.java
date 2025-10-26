package com.example.DsCatalog.services;

import com.example.DsCatalog.Dto.CategoryDTO;
import com.example.DsCatalog.Dto.ProductDTO;
import com.example.DsCatalog.entities.Category;
import com.example.DsCatalog.entities.Product;
import com.example.DsCatalog.projections.ProductProjection;
import com.example.DsCatalog.repository.CategoryRepository;
import com.example.DsCatalog.repository.ProductRepository;
import com.example.DsCatalog.services.excepetions.DatabaseException;
import com.example.DsCatalog.services.excepetions.ResourceNotFoundException;
import com.example.DsCatalog.util.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = repository.findAll(pageable);
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional
    public Page<ProductDTO> findAllPaged(String name,String categoryId, Pageable pageable){
        List<Long> categoryIds = Arrays.asList();
        if(!"0".equals(categoryId)){
            categoryIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
        }
        Page<ProductProjection> result = repository.searchProducts(categoryIds,name.trim(),pageable);
        List<Long> productIds = result.map(x->x.getId()).toList();

        List<Product> entities = repository.searchProductsWithCategories(productIds);
        entities = (List<Product>) Utils.replace(result.getContent(), entities);
        List<ProductDTO> dtos = entities.stream().map(p-> new ProductDTO(p,p.getCategories())).toList();

        Page<ProductDTO> pageDto = new PageImpl<>(dtos , result.getPageable(),result.getTotalElements());
        return pageDto;
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
