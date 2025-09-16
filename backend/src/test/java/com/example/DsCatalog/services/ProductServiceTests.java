package com.example.DsCatalog.services;

import com.example.DsCatalog.Dto.ProductDTO;
import com.example.DsCatalog.entities.Category;
import com.example.DsCatalog.entities.Product;
import com.example.DsCatalog.repository.CategoryRepository;
import com.example.DsCatalog.repository.ProductRepository;
import com.example.DsCatalog.repository.ProductRepositoryTests;
import com.example.DsCatalog.services.excepetions.DatabaseException;
import com.example.DsCatalog.services.excepetions.ResourceNotFoundException;
import com.example.DsCatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;


    private long existingId;
    private long nonExistingid;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1l;
        nonExistingid = 1000l;
        dependentId = 3l;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingid)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingid)).thenReturn(Optional.empty());

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistingid)).thenThrow(ResourceNotFoundException.class);


        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DatabaseException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void findAllShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(()->{
            service.delete(existingId);
        });
    }
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingid);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenIdExisting(){
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundException(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.findById(nonExistingid);
        });
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExisting(){
        ProductDTO exemplo = new ProductDTO(product);
        ProductDTO productDTO = service.update(existingId,exemplo);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void updateShouldThrowResourceNotFoundException(){
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            ProductDTO exemplo = new ProductDTO(product);
            service.update(nonExistingid,exemplo);
        });
    }
}
