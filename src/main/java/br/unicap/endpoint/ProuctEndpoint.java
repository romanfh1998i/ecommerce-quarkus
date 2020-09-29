package br.unicap.endpoint;



import br.unicap.model.Product;
import br.unicap.services.ProductService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/product")
public class ProuctEndpoint {

    @Inject
    ProductService productService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Product> getAll (Product p) {
        return productService.findAll();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product createProduct(Product p) {
        return productService.create(p);
    }

    @POST
    @Path("/reload")
    @Produces({MediaType.APPLICATION_JSON})
    public Response reload(Product p) {
        productService.fetchAll();
        return Response.accepted().build();
    }

}
