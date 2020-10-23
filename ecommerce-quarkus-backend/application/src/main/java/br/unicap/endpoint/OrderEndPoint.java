package br.unicap.endpoint;

import br.unicap.model.Order;
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
    Emitter<Order> orderEmitter;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Order createOrder(Order o) {
        orderEmitter.send(o);
        return o;
    }
}
