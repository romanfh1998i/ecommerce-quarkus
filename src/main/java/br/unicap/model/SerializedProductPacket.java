package br.unicap.model;

public class SerializedProductPacket {

    private String packetType;
    private Product p;

    public SerializedProductPacket(String packetType, Product p) {
        this.packetType = packetType;
        this.p = p;
    }

    public String getPacketType() {
        return packetType;
    }

    public void setPacketType(String packetType) {
        this.packetType = packetType;
    }

    public Product getP() {
        return p;
    }

    public void setP(Product p) {
        this.p = p;
    }
}
