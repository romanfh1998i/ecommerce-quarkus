package br.unicap.services;

import br.unicap.model.Product;
import br.unicap.model.serializeHelpers.realtimePackets.SerializedProductPacket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.ArrayList;

@ApplicationScoped
public class RealtimeService {

    private ArrayList<Session> connectedSessions = new ArrayList<>();

    public Session findSessionByID (String sessionId) {
        System.out.println("buscando");
        for (Session s : connectedSessions) {
            if (s.getId().equals(sessionId)) {
                return s;
            }
        }
        return null;
    }

    public void addSession(Session session) {
        connectedSessions.add(session);
    }

    public void removeSession(Session session) {
        connectedSessions.remove(session);
    }

    public void broadcast(String message) {
        this.connectedSessions.forEach((eachSession) -> {
            eachSession.getAsyncRemote().sendText(message);
        });
    }

    public void broadcastProductUpdate(Product p) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SerializedProductPacket packet = new SerializedProductPacket("update", p);
            String packetJson = objectMapper.writeValueAsString(packet);
            this.broadcast(packetJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
