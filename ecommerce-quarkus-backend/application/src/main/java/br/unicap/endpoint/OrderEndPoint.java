package br.unicap.endpoint;

import br.unicap.model.Cart;
import br.unicap.services.CartService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/order")
public class OrderEndPoint {

    @Inject
    @Channel("order-create")
    Emitter<Cart> orderEmitter;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Cart createOrder(Cart c) {
        orderEmitter.send(c);
        return c;
    }
}
