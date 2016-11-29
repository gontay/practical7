package demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerald on 30/11/2016.
 */
public class Message {
    final static String USER = "USERS";
    final static String MSG = "MSG";
    final static String ADMIN = "ADMIN";
    String type;
    String sender;
    List<String> messages;

    public Message(String type, String sender) {
        this.type = type;
        this.sender = sender;
    }
    public Message(String type) {
        this.type = type;
        this.sender = ADMIN;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String message) {
        if (messages == null) {
            messages = new ArrayList<String>();
        }
        messages.add(message);
    }
}
