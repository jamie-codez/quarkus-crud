package com.softech.crud.service;

import com.softech.crud.model.Product;
import com.softech.crud.repo.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Optional<Product> createProduct(Product product) {
        productRepository.persist(product);
        return Optional.of(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findByIdOptional(id);
    }

    public Optional<List<Product>> getAllProducts(){
        return productRepository.getAllProducts();
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public Optional<List<Product>> findByNameAndBrandName(String name, String brandName) {
        return productRepository.findByNameAndBrandName(name, brandName);
    }

    public Optional<Product> findByBrandName(String brandName) {
        return productRepository.findByBrandName(brandName);
    }

    public Optional<Product> findByPrice(Double price) {
        return productRepository.findByPrice(price);
    }

    @Transactional
    public Optional<Product> updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }

    @Transactional
    public Optional<Product> deleteProduct(Long id) {
        return productRepository.findByIdOptional(id).map(product -> {
            productRepository.delete(product);
            return product;
        });
    }
}
