package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeController {

    public JFXButton btnOrderDetails;
    public JFXButton btnPlaceOrder;
    public JFXButton btnItem;
    public JFXButton btnCustomer;
    public Label lblDate;
    public Label lblTime;
    public ImageView imgPane;
    public AnchorPane homePane;
    public void initialize(){
        manageTime();
        manageDate();
    }

    private void manageDate() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent ->lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")))),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void manageTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,
                actionEvent -> lblDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd:MM:yyyy")))),
                new KeyFrame(Duration.seconds(1)));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) homePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Customer.fxml"))));
            stage.setTitle("Customer");
            stage.show();
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnItemOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) homePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Item.fxml"))));
            stage.setTitle("Item");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) homePane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/PlaceOrder.fxml"))));
        stage.setTitle("Place Order");
        stage.show();
    }

    public void btnOrderDetailsOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) homePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/OrderDetails.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Order Details");
        stage.show();
    }
}
