package com.softech.crud.repo;

import com.softech.crud.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Optional<?> createProduct(Product product) {
        persist(product);
        return Optional.of(product);
    }

    public Optional<Product> findByName(String name) {
        return Optional.of(find("name", name).firstResult());
    }

    public Optional<Product> findByBrandName(String brandName) {
        return Optional.of(find("brandName", brandName).firstResult());
    }

    public Optional<List<Product>> getAllProducts(){
        return Optional.of(listAll());
    }

    public Optional<Product> findByPrice(Double price) {
        return Optional.of(find("price", price).firstResult());
    }

    public Optional<List<Product>> findByNameAndBrandName(String name, String brandName) {
        return Optional.of(list("name = ?1 and brandName = ?2", name, brandName));
    }

    public Optional<Product> updateProduct(Product product) {
        final var id = product.getId();
        if (id == null) {
            return Optional.empty();
        }
        var productOptional = findByIdOptional(id);
        if (productOptional.isEmpty()) {
            return Optional.empty();
        }
        var productFromDb = productOptional.orElseThrow();
        productFromDb.setName(product.getName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setBrandName(product.getBrandName());
        return Optional.of(productFromDb);
    }
}
