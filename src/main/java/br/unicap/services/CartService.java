package br.unicap.services;

import br.unicap.model.Cart;
import br.unicap.model.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.HashMap;

@ApplicationScoped
public class CartService {

    @Inject
    ProductService productService;

    @Inject
    RealtimeService realtimeService;

    HashMap<Session, Cart> activeCarts = new HashMap<>();

    public void addToCart(Session session, Long productId) {
        Cart c;
        if (activeCarts.containsKey(session)) {
            c = activeCarts.get(session);
        } else {
            c = new Cart();
        }


        Product p = productService.getById(productId);
        productService.handleCartAddition(p);
        c.getProducts().add(p);
        realtimeService.broadcastProductUpdate(p);

        activeCarts.put(session, c);
    }

    public void removeFromCart(Session session, Long productId) {
        Cart c = activeCarts.get(session);
        Product p = productService.getById(productId);
        productService.handleCartRemoval(p);
        c.getProducts().remove(p);
    }
}
