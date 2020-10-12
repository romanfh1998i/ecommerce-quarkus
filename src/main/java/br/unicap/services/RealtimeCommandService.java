package br.unicap.services;

import br.unicap.model.Order;
import br.unicap.model.Product;
import br.unicap.model.serializeHelpers.messages.SerializedCommand;
import br.unicap.services.realtimeCommandHandlers.AddToCartCommandService;
import br.unicap.services.realtimeCommandHandlers.GetCartCommandService;
import br.unicap.services.realtimeCommandHandlers.RealtimeCommand;
import br.unicap.services.realtimeCommandHandlers.RemoveFromCartCommandService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class RealtimeCommandService {

    @Inject
    AddToCartCommandService addToCartCommandService;

    @Inject
    GetCartCommandService getCartCommandService;

    @Inject
    RemoveFromCartCommandService removeFromCartCommandService;

    @Inject
    RealtimeService realtimeService;

    ConcurrentHashMap<String, RealtimeCommand> commandHandlers = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        this.commandHandlers.put("addToCart", addToCartCommandService);
        this.commandHandlers.put("removeFromCart", removeFromCartCommandService);
        this.commandHandlers.put("getCart", getCartCommandService);
    }

    @Incoming("command-executor")
    public void execute(String JsonSerializedCommand) {
        SerializedCommand serializedCommand = null;
        try {
            serializedCommand = new ObjectMapper().readValue(JsonSerializedCommand, SerializedCommand.class);
            String message = serializedCommand.getCommand();
            Session sessionFound = realtimeService.findSessionByID(serializedCommand.getSessionId());
            String[] parts = message.split(" ");
            RealtimeCommand cmd = this.commandHandlers.get(parts[0]);
            cmd.execute(sessionFound, parts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
