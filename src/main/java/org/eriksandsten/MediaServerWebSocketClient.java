package org.eriksandsten;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.eriksandsten.devtools.request.BaseRequest;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class MediaServerWebSocketClient extends WebSocketClient {
    public final BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
    private Map<String, Consumer<Object>> callbacks = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public MediaServerWebSocketClient(String serverUri) {
        super(URI.create(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to DevTools WebSocket");
    }

    public void addCallback(String eventName, Consumer<Object> callback) {
        callbacks.put(eventName, callback);
    }

    @Data
    public static class MethodObj {
        public String method;
        public Object params;
        public String sessionId;
    }

    @Override
    public void onMessage(String message) {
        System.out.println("\n==========================================================================================================\n");
        System.out.println("Received: " + message);
        System.out.println("\n==========================================================================================================\n\n");

        try {
            if (!message.startsWith("{\"method\"")) {
                queue.put(message); // queue.add("DEJSAN");
            } else {
                MethodObj methodObj = objectMapper.readValue(message, MethodObj.class);
                if (callbacks.containsKey(methodObj.getMethod())) {
                    callbacks.get(methodObj.getMethod()).accept(methodObj.getParams());
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex.getCause());
    }

    public Object executeCommand(String command, Class<?> clazz) {
        try {
            this.send(command);
            String response = queue.take();
            return objectMapper.readValue(response, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    public Object executeCommand(BaseRequest command, Class<?> clazz) {
        try {
            String json = command.getJSON();
            this.send(json);
            String response = queue.take();
            return objectMapper.readValue(response, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return new RuntimeException(e.getCause());
        }
    }
}
