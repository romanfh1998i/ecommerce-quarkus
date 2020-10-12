package br.unicap.model.serializeHelpers.messages;

public class SerializedCommand {
    private String sessionId;
    private String command;

    public SerializedCommand() {}

    public SerializedCommand(String sessionId, String command) {
        this.sessionId = sessionId;
        this.command = command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
