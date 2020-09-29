package br.unicap.services;

import br.unicap.model.Cart;
import br.unicap.model.Order;
import br.unicap.model.Product;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class OrderService extends BaseService<Order>{

    @Inject
    CartService cartService;

    @Inject
    ProductService productService;

    @Inject
    RealtimeService realtimeService;

    public OrderService() {
        super(Order.class);
    }

    public Order createOrder(Order o) {
        Cart c = cartService.findById(o.getCartId());
        List<Product> productsInCart = c.getProducts();
        Double totalPrice = 0D;
        for (Product eachProduct : productsInCart) {
            productService.handleOrderCreation(eachProduct);
            realtimeService.broadcastProductUpdate(eachProduct);
            totalPrice += eachProduct.getPrice();
        }
        o.setProducts(productsInCart);
        o.setTotalPrice(totalPrice);
        cartService.removeCart(c);
        return this.insert(o);
    }
}

