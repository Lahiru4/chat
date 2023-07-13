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

    private static ArrayList<Users> users = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(3002);
        while (true) {
            System.out.println("wit");
            Socket accept = serverSocket.accept();
            System.out.println("A   wit");
            DataOutputStream dataOutputStream = new DataOutputStream(accept.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(accept.getInputStream());
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    String user = null;
                    try {
                        user = dataInputStream.readUTF();
                        if (!isAvailable(user, users)) {
                            users.add(new Users(user, accept));
                            System.out.println("user:" + user);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (true) {
                        try {
                            System.out.println("awa mg pack");
                            String type = dataInputStream.readUTF();
                            System.out.println("type :"+type);
                            if (type.equals("sms")) {
                                System.out.println("in pack sms");
                                String user_name = dataInputStream.readUTF();
                                String user_mg = dataInputStream.readUTF();
                                sendAll(users, user_name, user_mg);
                                System.out.println("user name:" + user_name);
                                System.out.println("user mg:" + user_mg);
                            }
                            if (type.equals("img")) {
                                System.out.println("in pack img");
                                String size = dataInputStream.readUTF();
                                String user_name = dataInputStream.readUTF();
                                System.out.println(user_name+":img send");
                                byte[] blob = new byte[Integer.parseInt(size)];
                                dataInputStream.readFully(blob);
                                System.out.println(blob);
                                sendImg(users, user_name, blob);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            thread.start();
        }
    }

    private static void sendImg(ArrayList<Users> users, String userName, byte[] blob) throws IOException {
        for (Users client : users) {
            DataOutputStream outputStream = new DataOutputStream(client.getSocket().getOutputStream());
            if (!client.getName().equals(userName)) {
                try {
                    outputStream.writeUTF("img");
                    outputStream.writeUTF(blob.length+"");
                    outputStream.write(blob);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            if (!client.getName().equals(user_name)) {
                try {
                    outputStream.writeUTF("sms");
                    outputStream.writeUTF(user_mg);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}