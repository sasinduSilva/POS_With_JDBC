package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard_Controller implements Initializable {
    public Button btnManageCustomer;
    public Button btnManageItems;
    public Button btnPlaceOrder;
    public Button btnSearchOrders;
    public Button btnAbout;
    public Button btnContactUs;
    public AnchorPane root;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000),root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

    }

    public void navigate(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() instanceof Button){
            Button btn = (Button) actionEvent.getSource();

            Parent root = null;

            switch (btn.getId()){

                case "btnManageCustomer":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageCustomer.fxml"));
                    break;
                case "btnManageItems":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageItem.fxml"));
                    break;
                case "btnPlaceOrder":
                    root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrder.fxml"));
                    break;
                case "btnSearchOrders":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ViewOrders.fxml"));
                    break;




            }
            if (root!= null){
                Scene scene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350),scene.getRoot());
                tt.setFromX(-scene.getWidth());
                tt.setToX(0);
                tt.play();

            }

        }
    }

    public void playMouseEnteredAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Button) {
            Button btn = (Button) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), btn);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            btn.setEffect(glow);

        }
    }

    public void playMouseExitedAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Button){
            Button btn = (Button) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200),btn);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            btn.setEffect(null);

        }
    }
}
