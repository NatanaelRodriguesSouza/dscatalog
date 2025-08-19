package com.example.DsCatalog.Dto;

import com.example.DsCatalog.entities.Category;
import com.example.DsCatalog.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imgUrl;
    private List<CategoryDTO> categories = new ArrayList<>();
    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity){
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        for(Category s : entity.getCategories()){
            categories.add(new CategoryDTO(s));
        }
    }

    public Long getId() {
        return id;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
