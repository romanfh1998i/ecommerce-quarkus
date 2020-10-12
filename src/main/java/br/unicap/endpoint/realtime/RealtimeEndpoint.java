package br.unicap.endpoint.realtime;

import br.unicap.model.serializeHelpers.messages.SerializedCommand;
import br.unicap.services.CartService;
import br.unicap.services.RealtimeCommandService;
import br.unicap.services.RealtimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/realtime")
public class RealtimeEndpoint {

    @Inject
    RealtimeService realtimeService;

    @Inject
    RealtimeCommandService realtimeCommandService;

    @Inject
    @Channel("command-executor")
    Emitter<String> commandExecutorEmmiter;

    @OnOpen
    public void onOpen(Session session) {
        session.getAsyncRemote().sendText("hello");
        this.realtimeService.addSession(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws JsonProcessingException {
        SerializedCommand serializedCommand = new SerializedCommand(session.getId(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(serializedCommand);

        new Thread(() -> {
            commandExecutorEmmiter.send(json);
        }).start();
    }

    @OnClose
    public void onClose(Session session) {
        this.realtimeService.removeSession(session);
    }
}
