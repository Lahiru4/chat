package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChartFromController implements Initializable {


    public AnchorPane sms_area;
    public ScrollPane scroller_area;
    public AnchorPane mean;
    @FXML
    private Label user_name;

    public TextField smsTexFeld;
    public AnchorPane showBar;
    private boolean opt = true;
    public Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ArrayList<Label> get_sms=new ArrayList<>();
    private int y=662;
    private double height;


    public void sendUserOnAction(ActionEvent actionEvent) {
        sendSms();
    }

    private void sendSms() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //2
                    dataOutputStream.writeUTF("sms");
                    dataOutputStream.writeUTF(user_name.getText());
                    dataOutputStream.writeUTF(smsTexFeld.getText());
                    dataOutputStream.flush();
                    setMeMg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void setMeMg() {
        Platform.runLater(new Runnable() {
            public void run() {
                //update application thread
                Label label = new Label();
                label.setStyle("-fx-background-color: #1188ff;" +
                        "-fx-border-radius: 15;-fx-background-radius: 15;-fx-font-size: 15;-fx-font: bold;");
                label.setPrefHeight(30);

                label.setLayoutX(400);
                label.setLayoutY(y);
                y+=40;
                height+=100;
                //sms_area.setMaxHeight(height);
                label.setText("   "+smsTexFeld.getText()+"   ");
                //get_sms.add(label);
                sms_area.getChildren().add(label);
                scroller_area.setVvalue(1.0);
                smsTexFeld.clear();
            }
        });

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
        FileChooser chooser = new FileChooser();
        File file =chooser.showOpenDialog(mean.getScene().getWindow());
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            if (fileInputStream!=null) {
                Image image = new Image(fileInputStream);
                showBar.setVisible(false);
                byte[] blob = imagenToByte(image);
                sendImg(blob);
                setMyImg(image);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setMyImg(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setLayoutX(400);
        imageView.setLayoutY(y);
        sms_area.getChildren().add(imageView);
        scroller_area.setVvalue(1.0);

    }

    private void setImg(byte[] blob) {
        Platform.runLater(new Runnable() {
            public void run() {
                Image image = new Image(new ByteArrayInputStream(blob));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setLayoutX(400);
                imageView.setLayoutY(y);
                y+=140;
                sms_area.getChildren().add(imageView);
                scroller_area.setVvalue(1.0);
            }
        });
    }


    private void sendImg(byte[] blob) {
        //
        try {
            dataOutputStream.writeUTF("img");
            dataOutputStream.writeUTF(blob.length+"");
            dataOutputStream.writeUTF(user_name.getText());
            dataOutputStream.write(blob);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] imagenToByte(Image image) {
        BufferedImage bufferimage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferimage, "jpg", output );
            ImageIO.write(bufferimage, "png", output );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte [] data = output.toByteArray();
        return data;
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
            sms_area.getHeight();
            socket = new Socket("localhost", 3002);
            OutputStream outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataInputStream = new DataInputStream(socket.getInputStream());
            user_name.setText("eeeeeeeeeeeeeeee");
            sendSaverUserName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        String type = dataInputStream.readUTF();
                        if (type.equals("sms")) {
                            System.out.println("sms");
                            String sms = dataInputStream.readUTF();
                            setSms(sms);
                        }
                        if (type.equals("img")) {
                            System.out.println("get img");
                            String size = dataInputStream.readUTF();
                            byte[] blob =new byte[Integer.parseInt(size)];
                            dataInputStream.readFully(blob);
                            setImg(blob);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        thread.start();
    }

    private void setSms(String sms) {
        Platform.runLater(new Runnable() {
            public void run() {
                System.out.println("set sms");
                //update application thread
                System.out.println(sms);
                Label label = new Label();
                label.setStyle("-fx-background-color: #7aff11;" +
                        "-fx-border-radius: 15;-fx-background-radius: 15;-fx-font-size: 15;-fx-font: bold;");
                label.setPrefHeight(30);
                label.setLayoutX(20);
                label.setLayoutY(y);
                y+=40;
                height+=100;
                sms_area.setMaxHeight(height);
                label.setText("   "+sms+"   ");
                get_sms.add(label);
                sms_area.getChildren().add(label);
                scroller_area.setVvalue(1.0);
            }
        });
    }

    private void sendSaverUserName() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
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
