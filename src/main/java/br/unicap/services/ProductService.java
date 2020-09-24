package br.unicap.services;


import br.unicap.model.Product;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductService extends BaseService<Product>{

    List<Product> products = new ArrayList<>();

    public ProductService() {
        super(Product.class);
    }

    @PostConstruct
    public void postConstruct() {
        this.fetchAll();
    }

    public void fetchAll() {
        this.products.clear();
        this.products.addAll(this.findAll());
        System.out.println("Fetched " +  this.products.size() + " from the database.");
    }

}
