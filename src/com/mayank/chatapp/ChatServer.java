package com.mayank.chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ServerSocket serverSocket;

    public ChatServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void closeServerSocket() {
            try{
                if(serverSocket != null){
                    serverSocket.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket(1234);
        ChatServer server = new ChatServer(serverSocket1);
        server.startServer();
    }
}
