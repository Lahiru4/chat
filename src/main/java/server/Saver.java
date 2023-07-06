package server;

import dto.Users;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Saver {
    private static ServerSocket serverSocket;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static ArrayList<Users> users = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(3002);
        while (true) {
            Socket accept = serverSocket.accept();
            dataOutputStream = new DataOutputStream(accept.getOutputStream());
            dataInputStream = new DataInputStream(accept.getInputStream());
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    String user = null;
                    try {
                        user = dataInputStream.readUTF();
                        if (!isAvailable(user, users)) {
                            users.add(new Users(user, accept));
                            System.out.println(user);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (true){
                        try {
                            String user_name=dataInputStream.readUTF();
                            String user_mg=dataInputStream.readUTF();
                            sendAll(users,user_name,user_mg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            thread.start();


        }
    }

    private static boolean isAvailable(String name, ArrayList<Users> sockets) {
        for (Users usr : sockets) {
            if (usr.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    public static void sendAll(ArrayList<Users> clients, String user_name, String user_mg) throws IOException {
        for (Users client : clients) {
            DataOutputStream outputStream = new DataOutputStream(client.getSocket().getOutputStream());
            if (!client.getName().equals(user_name) ) {
                try {
                    outputStream.writeUTF(user_mg);
                    outputStream.flush();
                    System.out.println("Flushed: " + System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}