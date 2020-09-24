package br.unicap.endpoint.realtime;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/realtime")
public class RealtimeEndpoint {
    @OnOpen
    public void onOpen(Session session) {
        session.getAsyncRemote().sendText("hello");
    }
}
