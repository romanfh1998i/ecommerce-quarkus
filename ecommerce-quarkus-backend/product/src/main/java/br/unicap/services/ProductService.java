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
import javax.ws.rs.NotFoundException;
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
    public Product create(Product p)  {
        Product created = this.insert(p);
        this.products.put(created.getId(), created);
        return created;
    }

    public Product handleCartAdd(Long productId) {
        Product p = this.getById(productId);

        if (p == null) {
            throw new NotFoundException();
        }

        p.setAmountAvailable(p.getAmountAvailable() - 1);
        p.setAmountInCarts(p.getAmountInCarts() + 1);

        return this.update(p);

    }


    public Product handleCartRemove(Long productId) {

        Product p = this.getById(productId);

        if (p == null) {
            throw new NotFoundException();
        }

        p.setAmountAvailable(p.getAmountAvailable() + 1);
        p.setAmountInCarts(p.getAmountInCarts() - 1);

        return this.update(p);
    }

    public Product handleCartOrdered(Long  productId) {

        Product p = this.getById(productId);

        if (p == null) {
            throw new NotFoundException();
        }

        p.setAmountInCarts(p.getAmountInCarts() - 1);
        p.setAmountOrdered(p.getAmountOrdered() + 1);

        return this.update(p);
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

    public Product handleProductUpdateRequest(Long id, Product p) {
        Product productFound = this.findById(id);

        if (productFound == null) {
            throw new NotFoundException("Produto não encontrado");
        }

        if (p.getAmountOrdered() != null) {
            productFound.setAmountOrdered(p.getAmountOrdered());
        }
        if (p.getAmountInCarts() != null) {
            productFound.setAmountInCarts(p.getAmountInCarts());
        }
        if (p.getAmountAvailable() != null) {
            productFound.setAmountAvailable(p.getAmountAvailable());
        }

        return this.update(productFound);
    }
}
