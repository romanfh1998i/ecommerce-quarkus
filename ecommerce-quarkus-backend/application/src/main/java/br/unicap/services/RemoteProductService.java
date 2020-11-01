package br.unicap.services;

import br.unicap.model.Product;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/product")
@RegisterRestClient(configKey="product-api")
public interface RemoteProductService {

    @PUT
    @Path("/removeFromCart/{id:[0-9][0-9]*}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product removeFromCart(@PathParam("id") Long id);

    @PUT
    @Path("/addedToCart/{id:[0-9][0-9]*}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product addToCart(@PathParam("id") Long id);

    @PUT
    @Path("/ordered/{id:[0-9][0-9]*}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Product productOrdered(@PathParam("id") Long id);
}
