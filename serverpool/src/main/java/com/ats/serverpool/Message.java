package com.ats.serverpool;

public class Message {

    private String messageType;
    private String message;

    public Message() {
        this.messageType = "";
        this.message = "";
    }

    public Message(String messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] toBytes() {
        String packetMessage = getMessageType() + "\n" + getMessage();
        return packetMessage.getBytes();
    }
}