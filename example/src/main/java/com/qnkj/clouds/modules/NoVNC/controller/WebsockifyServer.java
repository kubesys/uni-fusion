package com.qnkj.clouds.modules.NoVNC.controller;


import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
public class WebsockifyServer extends WebSocketServer {

    public WebsockifyServer(InetSocketAddress address, String tcpHost, int tcpPort) {
        super(address);
        this.tcpHost = tcpHost;
        this.tcpPort = tcpPort;
    }

    private String tcpHost;
    private int tcpPort;

    HashMap<WebSocket,Socket> connections = new HashMap<>();

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //conn.send("Welcome to the server!"); //This method sends a message to the new client
        //broadcast("new connection: " + handshake.getResourceDescriptor()); //This method sends a message to all clients connected
        log.info("new connection to " + conn.getRemoteSocketAddress());

        try {
            //String getParams = handshake.getResourceDescriptor().split("\\?")[1]; // GET-Parameter abrufen
            //System.out.println("GET-Parameter: " + getParams);

            // Überprüfe den Wert des "token" GET-Parameters
            //String tokenValue = getParameterValue(getParams, "token");
            //if (!"Test".equals(tokenValue)) {
            //    conn.close();
            //    return;
            //}

            // TCP-Verbindung zum VNC-Server herstellen
            Socket tcpSocket = new Socket(tcpHost, tcpPort);

            // WebSocket-Session und TCP-Socket miteinander verbinden
            connections.put(conn,tcpSocket);

            // Starte den Thread zum Lesen der Daten vom VNC-Server und Senden an den WebSocket-Client
            new Thread(() -> forwardDataFromTcpToWebSocket(conn,tcpSocket)).start();

        }catch (Exception ex){
            ex.printStackTrace();
            conn.close();
        }



    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        Socket tcpSocket = connections.get(conn);
        if(!tcpSocket.isClosed()){
            try {
                tcpSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connections.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        //System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
        try {
            Socket tcpSocket = connections.get(conn);
            OutputStream os = tcpSocket.getOutputStream();
            os.write(message.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        //System.out.println("received ByteBuffer from " + conn.getRemoteSocketAddress());
        try {
            Socket tcpSocket = connections.get(conn);
            OutputStream os = tcpSocket.getOutputStream();
            byte[] data = new byte[message.remaining()];
            message.get(data);
            os.write(data);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
//        log.error("an error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        log.info("server started successfully");
    }

   // @Override
   // public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft,
   //                                                                    ClientHandshake request) throws InvalidDataException {
   //     ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
   //     //In this example we don't allow any resource descriptor ( "ws://localhost:8887/?roomid=1 will be rejected but ws://localhost:8887 is fine)
   //     if (!request.getResourceDescriptor().equals("/")) {
   //         throw new InvalidDataException(CloseFrame.POLICY_VALIDATION, "Not accepted!");
   //     }
   //
   //     return builder;
   // }

    private void forwardDataFromTcpToWebSocket(WebSocket wsConn, Socket tcpSocket) {
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;
            InputStream is = tcpSocket.getInputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
               //byte[] data = new byte[bytesRead];
               //System.arraycopy(buffer, 0, data, 0, bytesRead);
                // wsConn.send(data);
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
                wsConn.send(byteBuffer);
            }
        } catch (IOException e) {
        }
    }
    private String getParameterValue(String queryString, String parameterName) {
        String[] parameters = queryString.split("&");
        for (String parameter : parameters) {
            String[] keyValue = parameter.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(parameterName)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
