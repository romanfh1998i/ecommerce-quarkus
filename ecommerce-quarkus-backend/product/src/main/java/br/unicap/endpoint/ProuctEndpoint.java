package br.unicap.endpoint;

import br.unicap.model.Product;
import br.unicap.services.ProductService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/product")
public class ProuctEndpoint {

    @Inject
    ProductService productService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Collection<Product> getAll () {
        return productService.getAllProducts();
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

    @PUT
    @Path("/ordered/{id:[0-9][0-9]*}")
    @Produces({MediaType.APPLICATION_JSON})
    public Product productOrdered(@PathParam("id") Long id) {
        return this.productService.handleCartOrdered(id);
    }

    @PUT
    @Path("/addedToCart/{id:[0-9][0-9]*}")
    @Produces({MediaType.APPLICATION_JSON})
    public Product productAddedToCart(@PathParam("id") Long id) {
        return this.productService.handleCartAdd(id);
    }

    @PUT
    @Path("/removeFromCart/{id:[0-9][0-9]*}")
    @Produces({MediaType.APPLICATION_JSON})
    public Product productRemovedFromCart(@PathParam("id") Long id) {
        return this.productService.handleCartRemove(id);
    }

}
