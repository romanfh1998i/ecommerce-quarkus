package br.unicap.services;

import br.unicap.model.Cart;
import br.unicap.model.Product;
import br.unicap.model.SerializedCartPacket;
import br.unicap.model.SerializedProductPacket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.*;

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

    public void removeCart(Cart cart) {
        Set<Session> keys = new HashSet<Session>();
        for (Map.Entry<Session, Cart> entry : activeCarts.entrySet()) {
            if (entry.getValue().getId().equals(cart.getId())) {
                keys.add(entry.getKey());
            }
        }
        keys.forEach((eachSession) -> {
            activeCarts.remove(eachSession);
        });
    }

    public void sendCart(Session session) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SerializedCartPacket packet = new SerializedCartPacket("get", this.activeCarts.get(session));
            String packetJson = objectMapper.writeValueAsString(packet);
            session.getAsyncRemote().sendText(packetJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Cart findById(String cartId) {
        Collection<Cart> carts = this.activeCarts.values();
        for (Cart eachCart : carts) {
            if (eachCart.getId().equals(cartId)) {
                return eachCart;
            }
        }
        return null;
    }
}
