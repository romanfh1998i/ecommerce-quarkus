package br.unicap.endpoint;



import br.unicap.model.Product;
import br.unicap.services.ProductService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/product")
public class ProuctEndpoint {

    @Inject
    ProductService productService;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product createProduct(Product p) {
        return productService.insert(p);
    }

    @POST
    @Path("/reload")
    @Produces({MediaType.APPLICATION_JSON})
    public Response reload(Product p) {
        productService.fetchAll();
        return Response.accepted().build();
    }
}
