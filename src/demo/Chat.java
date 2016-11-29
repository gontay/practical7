package demo;

import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * Created by Gerald on 30/11/2016.
 */
@ServerEndpoint("/chat/{id}")
public class Chat {
    private static Set<Session> allSessions = new HashSet<Session>();
    private static List<String> userLists = new ArrayList<String>();
    @OnOpen
    public void register(@PathParam("id") String id, Session session){
        System.out.println("Registering id : " + id + " for session: " + session.getId());
        session.getUserProperties().put("id", id);
        allSessions.add(session);
        userLists.add(id);
        Message m = new Message(Message.USER);
        m.setMessages(userLists);
        Gson gson = new Gson();
        for (Session s : allSessions) {
            try {
                s.getBasicRemote().sendText(gson.toJson(m));
                System.out.println("Sent to " + s.getUserProperties().get("id") + " : " + gson.toJson(m));
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

    @OnMessage
    public void onMessage(String txt, Session session) throws IOException {
        //if (txt != "") {
            System.out.println("Received : " + txt);
            String id = (String) session.getUserProperties().get("id");
            Message m = new Message(Message.MSG, id);
            m.addMessage(txt);
            Gson gson = new Gson();
            for (Session s : allSessions) {
                try {
                    s.getBasicRemote().sendText(gson.toJson(m));
                    System.out.println("Sent : " + gson.toJson(m));
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }
       // }else{
            //System.out.println("Empty String");
        }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        String id = (String) session.getUserProperties().get("id");
        System.out.println(id + " has left");
        Gson gson = new Gson();
        allSessions.remove(session);
        userLists.remove(id);
        Message n = new Message(Message.USER);
        n.setMessages(userLists);
        for (Session s : allSessions) {
            try {
                s.getBasicRemote().sendText(gson.toJson(n));
                System.out.println("Sent to " + s.getUserProperties().get("id") + " : " + gson.toJson(n));
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());

    }

}



