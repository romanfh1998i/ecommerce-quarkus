package br.unicap.services;


import br.unicap.model.Product;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ProductService extends BaseService<Product>{

    ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();

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
    }

    public Product getById (Long id) {
        return products.get(id);
    }

    public void handleCartAddition(Product p) {
        p.setAmountAvailable(p.getAmountAvailable() - 1);
        p.setAmountInCarts(p.getAmountInCarts() + 1);
        this.updateProductAsync(p);
    }

    public void handleCartRemoval(Product p) {
        p.setAmountAvailable(p.getAmountAvailable() + 1);
        p.setAmountInCarts(p.getAmountInCarts() - 1);
        this.updateProductAsync(p);
    }

    public void handleOrderCreation(Product p) {
        p.setAmountInCarts(p.getAmountInCarts() - 1);
        p.setAmountOrdered(p.getAmountOrdered() + 1);
        this.updateProductAsync(p);
    }

    public void updateProductAsync (Product p) {
        Thread t = new Thread(() -> {
            this.update(p);
        });
        t.start();
    }

    public Product create(Product p) {
        Product created = this.insert(p);
        this.products.put(created.getId(), created);
        return created;
    }

}
