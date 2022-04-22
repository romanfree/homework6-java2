package com.geekbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws IOException{
        SRV s = new SRV();
        s.start();
        s.catchClient();
        new Thread(){
            public void run(){
                while (true){
                    String txt = null;
                    try {
                        txt = s.in.readLine();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    if(txt != null){
                        try{
                            s.sendMessage(txt);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
                .start();

        new Thread(){
            public void run(){
                try{
                    s.writeToConsole();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
                .start();
    }
}

class SRV {
    BufferedReader in = null;
    PrintWriter out = null;
    ServerSocket serverSocket = null;
    Socket socket = null;
    String input;
    BufferedReader console = null;

    void start(){
        try {
            serverSocket = new ServerSocket(1111);
        } catch (IOException e) {
            System.out.println("Не удается открыть порт 1111.");
            System.exit(1);
        }
        System.out.println("Сервер запущен. Ждем клиента...");
    }
    void catchClient() throws IOException{
        try {
            socket = serverSocket.accept();
            System.out.println("Клиент подключился.");
        } catch (IOException e){
            System.out.println("Не удалось подключиться.");
            System.exit(1);
        }

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    void sendMessage(String msg) throws IOException{
        if(msg.equalsIgnoreCase("exit")) close();
        out.println("MSG: " + msg);
        System.out.println(msg);
    }

    void close() throws IOException{
        out.close();
        in.close();
        socket.close();
        serverSocket.close();
    }

    void writeToConsole() throws IOException{
        while (true){
            console = new BufferedReader(new InputStreamReader(System.in));
            String txt = console.readLine();
            sendMessage(txt);
        }
    }
}