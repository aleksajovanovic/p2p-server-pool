public class Message {

    private String messageType;
    private String message;

    Message(String messageType,String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public String getMessageType() {return this.messageType;}
    public String getMessage() {return this.message;}
    public String getProtocol() {return this.protocol;}

    public byte[] toBytes() {
        String packetMessage = getMessageType() + "\n" + getMessage();
        return packetMessage.getBytes(); 
    }
}