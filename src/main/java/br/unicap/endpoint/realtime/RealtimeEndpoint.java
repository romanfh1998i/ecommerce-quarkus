package br.unicap.endpoint.realtime;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/realtime")
public class RealtimeEndpoint {
    @OnOpen
    public void onOpen(Session session) {
        session.getAsyncRemote().sendText("hello");
    }

    @OnMessage
    public void onOpen(Session session, String message) {
        session.getAsyncRemote().sendText(message);
    }
}
