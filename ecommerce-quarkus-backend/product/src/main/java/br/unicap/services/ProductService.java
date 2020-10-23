package br.unicap.services;


import br.unicap.model.Product;
import br.unicap.model.serializeHelpers.realtimePackets.SerializedProductPacket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
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

    @Inject
    @Channel("product-updated")
    Emitter<String> productUpdatedEmmiter;

    @Incoming("product-create")
    public Product create(Product p) throws JsonProcessingException {
        Product created = this.insert(p);
        this.products.put(created.getId(), created);
        return created;
    }

    @Incoming("cart-add-request")
    public void handleCartAdd(Long productId) throws JsonProcessingException {
        Product p = this.getById(productId);
        p.setAmountAvailable(p.getAmountAvailable() - 1);
        p.setAmountInCarts(p.getAmountInCarts() + 1);
        this.updateProductAsync(p);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(p);
        Thread t = new Thread(() -> {
            productUpdatedEmmiter.send(json);
        });
        t.start();
    }

    @Incoming("cart-remove-request")
    public void handleCartRemove(Long productId) {
        Product p = this.getById(productId);
        p.setAmountAvailable(p.getAmountAvailable() + 1);
        p.setAmountInCarts(p.getAmountInCarts() - 1);
        this.updateProductAsync(p);
    }

    @Incoming("cart-ordered-request")
    public void handleCartOrdered(List<Product> products) {
        for (Product eachProduct: products) {
            eachProduct.setAmountAvailable(eachProduct.getAmountAvailable() + 1);
            eachProduct.setAmountInCarts(eachProduct.getAmountInCarts() - 1);
            this.updateProductAsync(eachProduct);
        }
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
