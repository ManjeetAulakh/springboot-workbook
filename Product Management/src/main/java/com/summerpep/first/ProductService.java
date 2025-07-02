package com.summerpep.first;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    public Product createProduct(Product product){

        if(product.getName() == null || product.getName().isEmpty()){
            throw new IllegalArgumentException("Product name cannot be Empty");
        }

        Product newProduct = productRepo.save(product);
        return newProduct;
    }

    public List<Product> getAll(){
        return productRepo.findALL();
    }
}
