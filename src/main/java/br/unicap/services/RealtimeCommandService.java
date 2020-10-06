package br.unicap.services;

import br.unicap.services.realtimeCommandHandlers.AddToCartCommandService;
import br.unicap.services.realtimeCommandHandlers.GetCartCommandService;
import br.unicap.services.realtimeCommandHandlers.RealtimeCommand;
import br.unicap.services.realtimeCommandHandlers.RemoveFromCartCommandService;

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

    ConcurrentHashMap<String, RealtimeCommand> commandHandlers = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        this.commandHandlers.put("addToCart", addToCartCommandService);
        this.commandHandlers.put("removeFromCart", removeFromCartCommandService);
        this.commandHandlers.put("getCart", getCartCommandService);

    }

    public void execute(Session session, String message) {
        String[] parts = message.split(" ");
        RealtimeCommand cmd = this.commandHandlers.get(parts[0]);
        cmd.execute(session, parts);
    }

}
