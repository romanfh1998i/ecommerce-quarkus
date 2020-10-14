package br.unicap.services.realtimeCommandHandlers;

import br.unicap.services.CartService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

@ApplicationScoped
public class AddToCartCommandService implements RealtimeCommand{

    @Inject
    CartService cartService;

    @Inject
    @Channel("cart-add")
    Emitter<String> addToCart;

    @Override
    public void execute(Session session, String[] args) {

        cartService.addToCart(session, Long.parseLong(args[1]));
    }
}
