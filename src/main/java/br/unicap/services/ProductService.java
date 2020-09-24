package br.unicap.services;


import br.unicap.model.Product;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class ProductService extends BaseService<Product>{

    HashMap<Long, Product> products = new HashMap<>();

    public ProductService() {
        super(Product.class);
    }

    @PostConstruct
    public void postConstruct() {
        this.fetchAll();
    }

    public void fetchAll() {
        this.products.clear();
        this.findAll().forEach((eachProduct) -> {
            this.products.put(eachProduct.getId(), eachProduct);
        });
        System.out.println("Fetched " +  this.products.size() + " products from the database.");
    }

}
