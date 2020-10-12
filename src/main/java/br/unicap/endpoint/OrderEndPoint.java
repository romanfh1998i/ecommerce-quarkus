package br.unicap.endpoint;

import br.unicap.model.Order;
import br.unicap.model.Product;
import br.unicap.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    OrderService orderService;

    @Inject
    @Channel("order-create")
    Emitter<String> orderEmitter;


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Order createOrder(Order o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = objectMapper.writeValueAsString(o);
        orderEmitter.send(orderJson);
        return o;
    }
}
