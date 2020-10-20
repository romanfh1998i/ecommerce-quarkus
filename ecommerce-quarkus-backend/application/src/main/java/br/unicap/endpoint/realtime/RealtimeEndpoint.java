package br.unicap.endpoint.realtime;

import br.unicap.services.CartService;
import br.unicap.services.RealtimeCommandService;
import br.unicap.services.RealtimeService;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/realtime")
public class RealtimeEndpoint {

    @Inject
    RealtimeService realtimeService;

    @Inject
    RealtimeCommandService realtimeCommandService;

    @OnOpen
    public void onOpen(Session session) {
        session.getAsyncRemote().sendText("hello");
        this.realtimeService.addSession(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        realtimeCommandService.execute(session, message);
    }

    @OnClose
    public void onClose(Session session) {
        this.realtimeService.removeSession(session);
    }
}
