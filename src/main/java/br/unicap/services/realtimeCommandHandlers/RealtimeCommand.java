package br.unicap.services.realtimeCommandHandlers;

import javax.websocket.Session;

public interface RealtimeCommand {
    public void execute(Session session, String[] args);
}
