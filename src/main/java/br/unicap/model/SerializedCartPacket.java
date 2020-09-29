package br.unicap.model;

public class SerializedCartPacket {
    private String packetType;
    private Cart c;

    public SerializedCartPacket(String packetType, Cart c) {
        this.packetType = packetType;
        this.c = c;
    }

    public String getPacketType() {
        return packetType;
    }

    public void setPacketType(String packetType) {
        this.packetType = packetType;
    }

    public Cart getC() {
        return c;
    }

    public void setC(Cart c) {
        this.c = c;
    }
}
