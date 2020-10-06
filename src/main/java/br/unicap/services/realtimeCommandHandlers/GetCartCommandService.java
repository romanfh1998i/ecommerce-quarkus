package br.unicap.services.realtimeCommandHandlers;

import br.unicap.services.CartService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;

@ApplicationScoped
public class GetCartCommandService implements RealtimeCommand {

    @Inject
    CartService cartService;

    @Override
    public void execute(Session session, String[] args) {
        cartService.sendCart(session);
    }
}
