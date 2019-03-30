package com.ats.serverpool;

public class Message {

    private String messageType;
    private String message;

    public Message(String messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public String getMessage() {
        return this.message;
    }

    public byte[] toBytes() {
        String packetMessage = getMessageType() + "\n" + getMessage();
        return packetMessage.getBytes();
    }
}