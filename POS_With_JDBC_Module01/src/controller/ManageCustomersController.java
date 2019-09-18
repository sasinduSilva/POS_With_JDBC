package controller;

import DB.Arrays;
import DB.DBConnection;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CustoemrTM;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import util.OrdersTM;

public class ManageCustomersController implements Initializable {
    public Button btnHome;
    public Button btnManageItems;
    public Button btnPlaceOrder;
    public Button btnViewOrder;
    public Button btnNew;
    public JFXTextField txtCustmrId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtCntctNo;
    public Button btnSave;
    public Button btnDelete;
    public TableView<CustoemrTM> tblCstmrDetails;
    public AnchorPane root;

    public void navigate(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() instanceof Button){
            Button btn = (Button) actionEvent.getSource();

            Parent root = null;

            switch (btn.getId()){
                case "btnHome":
                    root = FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
                    break;

                case "btnManageItems" :
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageItem.fxml"));
                    break;

                case "btnPlaceOrder":
                    root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrder.fxml"));
                    break;

                case "btnViewOrder":
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

    public void btnNewOnAction(ActionEvent actionEvent) throws SQLException {
        ArrayList<CustoemrTM> custArray = new ArrayList<>();
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){

            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contactNo = resultSet.getString(4);
            custArray.add(new CustoemrTM(id,name,address,contactNo));

        }

        int arraySize = FXCollections.observableList(custArray).size()+2;
        String id = "";
        if (arraySize<10){

            id = "C00"+arraySize;

        }else if (arraySize<100){
            id = "C0"+arraySize;
        }else {
            id = "C"+arraySize;
        }
        txtCustmrId.setText(id);
        btnSave.setText("Save");

        txtName.clear();
        txtAddress.clear();
        txtCntctNo.clear();

        txtName.setDisable(false);
        txtAddress.setDisable(false);
        txtCntctNo.setDisable(false);


    }

    public void btnSaveOnAction(ActionEvent actionEvent) {

        String cid = txtCustmrId.getText();
        String name = null;
        String address = null;
        String cntctNo = null;



        if (btnSave.getText().equals("Update")){
            if (txtName.getText().matches("^[A-Z]{1}[a-z]+\\s{1}[A-Z]{1}[a-z]+$")){
                name = txtName.getText();



            }else {
                notification("Invalid Name",txtName);
                return;
            }
            if (txtAddress.getText().matches("([A-Za-z0-9,./-]+\\s?)+")){
                address = txtAddress.getText();
            }else {
                notification("Invalid Address",txtAddress);
                return;
            }
            if (txtCntctNo.getText().matches("^[1-9]{2}[-]{1}[1-9]{7}$")){
                cntctNo = txtCntctNo.getText();
            }else {
                notification("Invalid number",txtCntctNo);
                return;
            }

            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "UPDATE customer set name='"+name+"',address='"+address+"',contactNo='"+cntctNo+"' WHERE Id='"+txtCustmrId.getText()+"'";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows>0){

                    Alert successfully_updates = new Alert(Alert.AlertType.INFORMATION, "Successfully Updates", ButtonType.OK);
                    Optional<ButtonType> buttonType = successfully_updates.showAndWait();
                    if (buttonType.get()==ButtonType.OK){
                        for (CustoemrTM item : tblCstmrDetails.getItems()) {

                            if (item.getId().equals(cid)){

                                item.setName(name);
                                item.setAddress(address);
                                item.setContactNo(cntctNo);
                            }




                        }
                        tblCstmrDetails.refresh();




                    }


                }


            } catch (SQLException e) {
                e.printStackTrace();
            }


//            CustoemrTM selectedItem = tblCstmrDetails.getSelectionModel().getSelectedItem();
//            selectedItem.setName(name);
//            selectedItem.setAddress(address);
//            selectedItem.setContactNo(cntctNo);
            tblCstmrDetails.refresh();

            txtCustmrId.clear();
            txtName.clear();
            txtAddress.clear();
            txtCntctNo.clear();

            txtName.setDisable(true);
            txtAddress.setDisable(true);
            txtCntctNo.setDisable(true);
            return;
        }else {
            if (txtName.getText().matches("^[A-Z]{1}[a-z]+\\s{1}[A-Z]{1}[a-z]+$")){
                name = txtName.getText();



            }else {
                notification("Invalid Name",txtName);
                return;
            }
            if (txtAddress.getText().matches("([A-Za-z0-9,./-]+\\s?)+")){
                address = txtAddress.getText();
            }else {
                notification("Invalid Address",txtAddress);
                return;
            }
            if (txtCntctNo.getText().matches("^[1-9]{2}[-]{1}[1-9]{7}$")){
                cntctNo = txtCntctNo.getText();
            }else {
                notification("Invalid number",txtCntctNo);
                return;
            }

            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO customer values(?,?,?,?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1,cid);
                preparedStatement.setString(2,name);
                preparedStatement.setString(3,address);
                preparedStatement.setString(4,cntctNo);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows>0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Added", ButtonType.OK);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get()==ButtonType.OK){

                        tblCstmrDetails.getItems().add(new CustoemrTM(cid,name,address,cntctNo));
                        tblCstmrDetails.refresh();


                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            txtCustmrId.clear();
            txtName.clear();
            txtAddress.clear();
            txtCntctNo.clear();

            txtName.setDisable(true);
            txtAddress.setDisable(true);
            txtCntctNo.setDisable(true);
        }




    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        CustoemrTM selectedItem = tblCstmrDetails.getSelectionModel().getSelectedItem();
        if (selectedItem!=null){
            String cid = selectedItem.getId();
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "Delete from customer where Id='"+cid+"'";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                int affectedVal = preparedStatement.executeUpdate();

                if (affectedVal>0){
                    Alert deleted = new Alert(Alert.AlertType.INFORMATION, "Deleted", ButtonType.OK);
                    Optional<ButtonType> buttonType = deleted.showAndWait();
                    if (buttonType.get()==ButtonType.OK){
                        tblCstmrDetails.getItems().remove(selectedItem);
                        tblCstmrDetails.refresh();

                    }


                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        tblCstmrDetails.refresh();
        txtName.clear();
        txtAddress.clear();
        txtCntctNo.clear();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnDelete.setDisable(true);
        txtCustmrId.setDisable(true);
        txtCustmrId.setDisable(true);
        txtName.setDisable(true);
        txtAddress.setDisable(true);
        txtCntctNo.setDisable(true);

        tblCstmrDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("Id"));
        tblCstmrDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCstmrDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblCstmrDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contactNo"));


        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String contactNo = resultSet.getString(4);

                tblCstmrDetails.getItems().add(new CustoemrTM(id,name,address,contactNo));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }





        tblCstmrDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustoemrTM>() {
            @Override
            public void changed(ObservableValue<? extends CustoemrTM> observable, CustoemrTM oldValue, CustoemrTM newValue) {
                CustoemrTM selectedItem = tblCstmrDetails.getSelectionModel().getSelectedItem();
                if (selectedItem!=null){

                    txtCustmrId.setText(selectedItem.getId());
                    txtName.setText(selectedItem.getName());
                    txtAddress.setText(selectedItem.getAddress());
                    txtCntctNo.setText(selectedItem.getContactNo());

                    btnDelete.setDisable(false);
                    btnSave.setText("Update");

                    txtName.setDisable(false);
                    txtAddress.setDisable(false);
                    txtCntctNo.setDisable(false);

                    txtName.requestFocus();
                }
            }
        });


    }
    public void notification(String msg,JFXTextField id){
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get()==ButtonType.OK){
            id.requestFocus();

        }
    }

}
