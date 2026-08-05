package com.mayank.chatapp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> ClinetHandlers = new ArrayList<>();//purpose of this arraylist  here is to keep track of all client
    private Socket socket;
    private BufferedReader bufferreader;
    private BufferedWriter bufferwriter;
    private String clientusername;
    ClientHandler(Socket socket) {
        try { // it throw IO error to handle that exception handling used
            this.socket = socket;
            this.bufferwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientusername = bufferreader.readLine();
            ClinetHandlers.add(this);
            broadcastMessage("Server"+clientusername+"has just joined the chat ");
        }catch (IOException e){
            closeEverything(socket,bufferreader,bufferwriter);
        }
    }
    @Override
    public void run() {
        String Messagefromclient;
        while(socket.isConnected()){
            try{
                Messagefromclient = bufferreader.readLine();
                broadcastMessage(Messagefromclient);
            }catch (IOException e){
                closeEverything(socket,bufferreader,bufferwriter);
                break;
            }
        }
    }
    public void broadcastMessage(String MessagetoSend){
        for(ClientHandler clientHandler:ClinetHandlers){
            try{
                if(!clientHandler.clientusername.equals(clientusername)){
                    clientHandler.bufferwriter.write(MessagetoSend);
                    clientHandler.bufferwriter.newLine();
                    clientHandler.bufferwriter.flush();
                }
            }catch (IOException e) {
                closeEverything(socket, bufferreader, bufferwriter);
            }
        }
    }
    public void removeclient(){
        ClinetHandlers.remove(this);
        broadcastMessage("Server " +clientusername+ " has left the chat ");
    }
    public void closeEverything(Socket socket ,BufferedReader bufferreader ,  BufferedWriter bufferwriter){
        removeclient();
        try{
            if(bufferreader!=null){
                bufferreader.close();
            }
            if(bufferwriter!=null) {
                bufferwriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
