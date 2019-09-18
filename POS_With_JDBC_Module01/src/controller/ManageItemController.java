package controller;

import DB.DBConnection;
import DB.itemArray;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.itemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageItemController implements Initializable {
    public Button btnHome;
    public Button btnManageCstmr;
    public Button btnPlaceOrdr;
    public Button btnViewOrdr;
    public Button btnNw;
    public JFXTextField txtItemCode;
    public JFXTextField txtDesc;
    public JFXTextField txtQtyOnHnd;
    public JFXTextField txtUnitPrz;
    public Button btnSave;
    public Button btnDelete;
    public TableView<itemTM> tblItemDetails;
    public AnchorPane root;

    public void navigate(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() instanceof Button){
            Button btn = (Button) actionEvent.getSource();

            Parent root = null;

            switch (btn.getId()){
                case "btnHome":
                    root = FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
                    break;

                case "btnManageCstmr" :
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageCustomer.fxml"));
                    break;

                case "btnPlaceOrdr":
                    root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrder.fxml"));
                    break;

                case "btnViewOrdr":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ViewOrders.fxml"));
                    break;

            }
            if (root!=null){
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

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String itemCode = txtItemCode.getText();
        String desc = txtDesc.getText();
        int qtyOnHand;
        double unitPrz;

        if (btnSave.getText().equals("Update")){
            if (txtQtyOnHnd.getText().matches("^[0-9]+$")){
                qtyOnHand = Integer.parseInt(txtQtyOnHnd.getText());

            }else {
                notification("Invalid Quantity",txtQtyOnHnd);
                return;
            }
            if (txtUnitPrz.getText().matches("^[0-9]+[.]{1}[0-9]{2}$")){
                unitPrz= Double.parseDouble(txtUnitPrz.getText());


            }else {
                notification("invalid Price",txtUnitPrz);
                return;
            }

            itemTM selectedItem = tblItemDetails.getSelectionModel().getSelectedItem();
            if (selectedItem!= null){
                Connection connection = DBConnection.getInstance().getConnection();
                String sql = "update Item set description='"+desc+"',qtyOnHand='"+qtyOnHand+"',unitPrice='"+unitPrz+"' WHERE itemCode='"+txtItemCode.getText()+"'";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    int affctdRows = preparedStatement.executeUpdate();
                    if (affctdRows>0){
                        Alert updated = new Alert(Alert.AlertType.INFORMATION, "Updated", ButtonType.OK);
                        Optional<ButtonType> buttonType = updated.showAndWait();
                        if (buttonType.get()==ButtonType.OK){
                            selectedItem.setDescription(desc);
                            selectedItem.setQtyOnHand(qtyOnHand);
                            selectedItem.setUnitPrice(unitPrz);
                            tblItemDetails.refresh();


                        }

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                    txtItemCode.clear();
                    txtDesc.clear();
                    txtDesc.setDisable(true);
                    txtQtyOnHnd.clear();
                    txtQtyOnHnd.setDisable(true);
                    txtUnitPrz.clear();
                    txtUnitPrz.setDisable(true);

            }


        }else{
            if (txtQtyOnHnd.getText().matches("^[0-9]+$")){
                qtyOnHand = Integer.parseInt(txtQtyOnHnd.getText());

            }else {
                notification("Invalid Quantity",txtQtyOnHnd);
                return;
            }
            if (txtUnitPrz.getText().matches("^[0-9]+[.]{1}[0-9]{2}$")){
                unitPrz= Double.parseDouble(txtUnitPrz.getText());


            }else {
                notification("invalid Price",txtUnitPrz);
                return;
            }

            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT into Item values(?,?,?,?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,itemCode);
                preparedStatement.setString(2,desc);
                preparedStatement.setString(3,qtyOnHand+"");
                preparedStatement.setString(4,unitPrz+"");
                int affectedRow = preparedStatement.executeUpdate();

                if (affectedRow>0){
                    Alert added = new Alert(Alert.AlertType.INFORMATION, "Added", ButtonType.OK);
                    Optional<ButtonType> buttonType = added.showAndWait();
                    if (buttonType.get()==ButtonType.OK){
                        tblItemDetails.getItems().add(new itemTM(itemCode,desc,qtyOnHand,unitPrz));
                        tblItemDetails.refresh();


                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            tblItemDetails.refresh();

            txtItemCode.clear();
            txtDesc.clear();
            txtUnitPrz.clear();
            txtQtyOnHnd.clear();


            txtDesc.setDisable(true);
            txtUnitPrz.setDisable(true);
            txtQtyOnHnd.setDisable(true);

            btnSave.setDisable(true);



        }



    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        itemTM selectedItem = tblItemDetails.getSelectionModel().getSelectedItem();
        try {
          Connection connection = DBConnection.getInstance().getConnection();
          String sql = "DELETE FROM Item WHERE itemCode='"+txtItemCode.getText()+"'";
          PreparedStatement preparedStatement = connection.prepareStatement(sql);
          int rowCount = preparedStatement.executeUpdate();
          if (rowCount>0){
              Alert deleted = new Alert(Alert.AlertType.INFORMATION, "Deleted", ButtonType.OK);
              Optional<ButtonType> buttonType = deleted.showAndWait();
              if (buttonType.get()==ButtonType.OK){
                  tblItemDetails.getItems().remove(selectedItem);
                  tblItemDetails.refresh();

              }

          }
            btnNwOnAction(actionEvent);


      }catch (SQLException ex){
          ex.printStackTrace();

      }


    }

    public void btnNwOnAction(ActionEvent actionEvent) throws SQLException {
        itemArray itemArr = new itemArray();
        int arraySize = FXCollections.observableList(itemArr.itemArray).size()+1;
        String id = "";
        if (arraySize<10){

            id = "I00"+arraySize;

        }else if (arraySize<100){
            id = "I0"+arraySize;
        }else {
            id = "I"+arraySize;
        }
        txtItemCode.setText(id);
        btnSave.setText("Save");
        btnSave.setDisable(false);

        txtDesc.setDisable(false);
        txtQtyOnHnd.setDisable(false);
        txtUnitPrz.setDisable(false);

        txtDesc.clear();
        txtQtyOnHnd.clear();
        txtUnitPrz.clear();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblItemDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblItemDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        tblItemDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Item";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String itemCode = resultSet.getString(1);
                String desc = resultSet.getString(2);
                String qtyOnHand = resultSet.getString(3);
                String unitPrice = resultSet.getString(4);

                tblItemDetails.getItems().add(new itemTM(itemCode,desc,Integer.parseInt(qtyOnHand),Double.parseDouble(unitPrice)));




            }
            tblItemDetails.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtItemCode.setDisable(true);
        txtDesc.setDisable(true);
        txtQtyOnHnd.setDisable(true);
        txtUnitPrz.setDisable(true);
        btnSave.setDisable(true);

        tblItemDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<itemTM>() {
            @Override
            public void changed(ObservableValue<? extends itemTM> observable, itemTM oldValue, itemTM newValue) {
                itemTM selectedItem = tblItemDetails.getSelectionModel().getSelectedItem();
                if (selectedItem!=null){

                    btnSave.setText("Update");
                    btnSave.setDisable(false);

                   txtItemCode.setText(selectedItem.getItemCode());
                    txtDesc.setText(selectedItem.getDescription());
                    txtQtyOnHnd.setText(selectedItem.getQtyOnHand()+"");
                    txtUnitPrz.setText(selectedItem.getUnitPrice()+"");

                    txtDesc.setDisable(false);
                    txtQtyOnHnd.setDisable(false);
                    txtUnitPrz.setDisable(false);
                }
            }
        });


    }

    public void notification(String msg,JFXTextField id) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.OK) {
            id.requestFocus();

        }
    }
}
