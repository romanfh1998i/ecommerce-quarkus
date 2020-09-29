package br.unicap.endpoint.realtime;

import br.unicap.services.CartService;
import br.unicap.services.RealtimeService;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/realtime")
public class RealtimeEndpoint {

    @Inject
    RealtimeService realtimeService;

    @Inject
    CartService cartService;

    @OnOpen
    public void onOpen(Session session) {
        session.getAsyncRemote().sendText("hello");
        this.realtimeService.addSession(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (message.startsWith("addToCart")) {
            String[] parts = message.split(" ");
            cartService.addToCart(session, Long.parseLong(parts[1]));

        } else if (message.startsWith("removeFromCart")) {
            String[] parts = message.split(" ");
            cartService.removeFromCart(session, Long.parseLong(parts[1]));
        } else if (message.startsWith("getCart")) {
            cartService.sendCart(session);
        }
    }

    @OnClose
    public void onClose(Session session) {
        this.realtimeService.removeSession(session);
    }
}
