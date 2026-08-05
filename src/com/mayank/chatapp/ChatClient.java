package com.mayank.chatapp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private BufferedReader bufferreader;
    private BufferedWriter bufferwriter;
    private String username;
    public ChatClient(Socket socket, String username){
        try{
            this.socket = socket;
            this.username =username;
            this.bufferwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            closeEverything(socket,bufferreader,bufferwriter);
        }
    }
    public void sendMessage(){
        try{
            bufferwriter.write(username);
            bufferwriter.newLine();
            bufferwriter.flush();
            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String messagetoSend = sc.nextLine();
                bufferwriter.write(username + ": "+messagetoSend);
                bufferwriter.newLine();
                bufferwriter.flush();
            }
        }catch(IOException e){
            closeEverything(socket,bufferreader,bufferwriter);
        }
    }
    public void listenMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String MsgFromGroupChat;
                try{
                    while(socket.isConnected()){
                        MsgFromGroupChat = bufferreader.readLine();
                        System.out.println(MsgFromGroupChat);
                    }
                }catch(IOException e){
                    closeEverything(socket,bufferreader,bufferwriter);
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket ,BufferedReader bufferreader ,  BufferedWriter bufferwriter){
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

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter your username");
        String username = sc.nextLine();
        Socket socket = new Socket("localhost",1234);
        ChatClient client = new ChatClient(socket,username);
        client.listenMessage();
        client.sendMessage();
    }
}
