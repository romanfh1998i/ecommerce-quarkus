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
import java.util.Collection;
import java.util.List;

@Path("/product")
public class ProuctEndpoint {

    @Inject
    ProductService productService;

    @Inject
    @Channel("product-create")
    Emitter<Product> productEmitter;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Collection<Product> getAll () {
        return productService.getAllProducts();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product createProduct(Product p) {
        productEmitter.send(p);
        return p;
    }

    @POST
    @Path("/reload")
    @Produces({MediaType.APPLICATION_JSON})
    public Response reload(Product p) {
        productService.fetchAll();
        return Response.accepted().build();
    }

}
