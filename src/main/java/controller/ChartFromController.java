package controller;

import dto.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChartFromController implements Initializable {
    @FXML
    private Label user_name;

    public TextField smsTexFeld;
    public AnchorPane showBar;
    private boolean opt = true;
    public Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;


    public void sendUserOnAction(ActionEvent actionEvent) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //2
                    dataOutputStream.writeUTF(user_name.getText());
                    dataOutputStream.writeUTF(smsTexFeld.getText());
                    dataOutputStream.flush();
                    smsTexFeld.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    public void show(MouseEvent mouseEvent) {
        showBar.setVisible(true);
    }

    public void hide(MouseEvent mouseEvent) {
        if (opt) {
            showBar.setVisible(false);
        }

    }

    public void showBarOnAction(MouseEvent mouseEvent) {
        showBar.setVisible(true);
        opt = false;
    }

    public void addImgOnAction(MouseEvent mouseEvent) {

    }

    public void addFileOnAction(MouseEvent mouseEvent) {

    }

    public void hideShoeBar(MouseEvent mouseEvent) {
        showBar.setVisible(false);
        opt = true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket("localhost", 3002);
            OutputStream outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataInputStream = new DataInputStream(socket.getInputStream());
            user_name.setText("kumara");
            sendSaverUserName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                
            }
        };
    }

    private void sendSaverUserName() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //1
                    dataOutputStream.writeUTF(user_name.getText());
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
