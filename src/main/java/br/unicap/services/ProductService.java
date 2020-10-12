package br.unicap.services;


import br.unicap.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
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

    @Incoming("product-create")
    public Product create(String serializedProduct) throws JsonProcessingException {
        Product p = new ObjectMapper().readValue(serializedProduct, Product.class);
        Product created = this.insert(p);
        this.products.put(created.getId(), created);
        return created;
    }

    @Incoming("product-reload")
    public void reload(String reloadProduct){
        this.fetchAll();
    }

    public void fetchAll() {
        this.products.clear();
        this.findAll().forEach((eachProduct) -> {
            this.products.put(eachProduct.getId(), eachProduct);
            System.out.println(eachProduct.getAmountAvailable());
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

    public Collection<Product> getAllProducts() {
        return this.products.values();
    }

}
