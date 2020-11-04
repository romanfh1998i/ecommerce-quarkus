package br.unicap.services;


import br.unicap.model.Product;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import java.util.Collection;
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

}
