package br.unicap.endpoint;

import br.unicap.model.Product;
import br.unicap.services.ProductService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/product")
public class ProuctEndpoint {

    @Inject
    ProductService productService;

    @Channel("product-create")
    Emitter<Product> productCreateEmitter;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Collection<Product> getAll () {
        return productService.getAllProducts();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product createProduct(Product p) {
        productCreateEmitter.send(p);
        return p;
    }

    @POST
    @Path("/reload")
    @Produces({MediaType.APPLICATION_JSON})
    public Response reload(Product p) {
        productService.fetchAll();
        return Response.accepted().build();
    }

    @PUT
    @Path("/product/{id:[0-9][0-9]*}")
    @Produces({MediaType.APPLICATION_JSON})
    public Product updateProduct(@PathParam("id") Long id, Product p) {
        return this.productService.handleProductUpdateRequest(id, p);

    }

}
