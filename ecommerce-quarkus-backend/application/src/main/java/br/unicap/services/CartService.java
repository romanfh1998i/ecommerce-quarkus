package br.unicap.services;

import br.unicap.model.Cart;
import br.unicap.model.Product;
import br.unicap.model.serializeHelpers.realtimePackets.SerializedCartPacket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CartService extends BaseService<Cart>{

    @Inject
    RealtimeService realtimeService;

    @RestClient
    RemoteProductService remoteProductService;

    ConcurrentHashMap<Session, Cart> activeCarts = new ConcurrentHashMap();

    public CartService() {
        super(Cart.class);
    }

    @Incoming("order-create")
    public Cart createOrder(Cart c) {
        List<Product> productsInCart = this.findById(c.getCartId()).getProducts();
        for (Product eachProduct : productsInCart) {
            Product updatedProduct = this.remoteProductService.productOrdered(eachProduct.getId());
            this.realtimeService.broadcastProductUpdate(updatedProduct);
        }
        c.setProducts(productsInCart);
        Cart insertedCart = this.insert(c);
        this.removeCart(c);
        return insertedCart;
    }

    public void addToCart(Session session, Long productId) {
        Cart c;
        if (activeCarts.containsKey(session)) {
            c = activeCarts.get(session);
        } else {
            c = new Cart();
            c.setProducts((new ArrayList<Product>()).subList(0, 0));
        }

        Product updatedProduct = this.remoteProductService.addToCart(productId);
        this.realtimeService.broadcastProductUpdate(updatedProduct);

        c.getProducts().add(new Product(productId));

        activeCarts.put(session, c);
    }

    public void removeFromCart(Session session, Long productId) {
        Cart c = activeCarts.get(session);
        Product toRemove = null;
        for (Product eachProductInCart : c.getProducts()) {
            if (eachProductInCart.getId().equals(productId)) {
                toRemove = eachProductInCart;
                break;
            }
        }
        if (toRemove != null) {
            c.getProducts().remove(toRemove);
        }
        Product updatedProduct = this.remoteProductService.removeFromCart(productId);
        this.realtimeService.broadcastProductUpdate(updatedProduct);
    }

    public void removeCart(Cart cart) {
        Set<Session> keys = new HashSet<Session>();
        for (Map.Entry<Session, Cart> entry : activeCarts.entrySet()) {
            if (entry.getValue().getCartId().equals(cart.getCartId())) {
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
            if (eachCart.getCartId().equals(cartId)) {
                return eachCart;
            }
        }
        return null;
    }
}
