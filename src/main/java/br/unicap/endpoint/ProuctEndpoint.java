package br.unicap.endpoint;



import br.unicap.model.Product;
import br.unicap.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/product")
public class ProuctEndpoint {

    @Inject
    ProductService productService;

    @Inject
    @Channel("product-create")
    Emitter<String> productEmitter;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Product> getAll () {
        return productService.findAll();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product createProduct(Product p) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(p);
        productEmitter.send(productJson);
        return p;
    }

    @Inject
    @Channel("product-reload")
    Emitter<String> reloadEmitter;

    @POST
    @Path("/reload")
    public Response reload() {
        reloadEmitter.send("Reload products");
        return Response.accepted().build();
    }

}
