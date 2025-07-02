package com.summerpep.first.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.summerpep.first.models.Product;

@Repository
public class ProductRepo {
    private final List<Product> products = new ArrayList<>();
    private int id;

    public Product save(Product product){
        product.setId(id++);
        products.add(product);
        return product;
    }

    public Optional<Product> findById(int id){
        return products.stream().filter( p -> p.getId() == (id)).findFirst();
    }

    public List<Product> findALL(){
        return new ArrayList<>(products);
    }
}
