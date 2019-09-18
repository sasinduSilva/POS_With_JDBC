package controller;

import DB.DBConnection;
import DB.OrdersArray;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.OrderDetailTM;
import util.OrdersTM;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaceOrderController implements Initializable {
    public Button btnHome;
    public Button btnManageCstmr;
    public Button btnManageItm;
    public Button btnViewOrdrs;
    public Button btnNw;
    public Button btnAdd;
    public Button btnPlaceOrdr;
    public JFXComboBox<String> cmbCstmrId;
    public JFXComboBox<String> cmbItmCode;
    public JFXTextField txtCstmrName;
    public JFXTextField txtItemDesc;
    public JFXTextField txtQtyOnHnd;
    public JFXTextField txtUntPrz;
    public JFXTextField txtQty;
    public TableView<OrderDetailTM> tblOrdrs;
    public Label lblTotal;
    public AnchorPane root;
    public Label lblDate;
    public Label lblOrdrID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lblDate.setText(String.valueOf(LocalDate.now()));

        try {
            OrdersArray Or = new OrdersArray();


            int arraySize = FXCollections.observableList(Or.orderArray).size() + 1;
            String id = "";
            if (arraySize < 10) {

                id = "OD00" + arraySize;

            } else if (arraySize < 100) {
                id = "OD0" + arraySize;
            } else {
                id = "OD" + arraySize;
            }
            lblOrdrID.setText(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        btnAdd.setDisable(true);
        txtCstmrName.setDisable(true);
        txtItemDesc.setDisable(true);
        txtQtyOnHnd.setDisable(true);
        txtUntPrz.setDisable(true);


        ObservableList<String> items = cmbCstmrId.getItems();
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String cid = resultSet.getString(1);
                items.add(cid);


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "1");
        }

        ObservableList<String> itemCodes = cmbItmCode.getItems();
        String itemCodeSql = "SELECT * FROM Item";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(itemCodeSql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String itmCode = resultSet.getString(1);
                itemCodes.add(itmCode);


            }

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "2");
        }

        cmbCstmrId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String selectedItem = cmbCstmrId.getSelectionModel().getSelectedItem();

                try {
                    if (selectedItem != null) {
                        Connection conn = DBConnection.getInstance().getConnection();
                        String sql001 = "SELECT * FROM customer WHERE Id=?";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql001);
                        preparedStatement.setString(1, newValue);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        String id = resultSet.getString(1);
                        String name = resultSet.getString(2);
                        String address = resultSet.getString(3);
                        String contctNo = resultSet.getString(4);

                        txtCstmrName.setText(name);


                    } else {
                        return;
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
        });

        cmbItmCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                btnAdd.setText("Add");
                String selectedItem = cmbItmCode.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    btnAdd.setDisable(false);
                }


                String sql = "SELECT * FROM Item WHERE itemCode='" + selectedItem + "'";
                try {
                    if (selectedItem != null) {
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        txtItemDesc.setText(resultSet.getString(2));
                        txtUntPrz.setText(resultSet.getString(4));
                        txtQtyOnHnd.setText(resultSet.getString(3));
                    } else {
                        return;

                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        tblOrdrs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderDetailTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderDetailTM> observable, OrderDetailTM oldValue, OrderDetailTM newValue) {
                OrderDetailTM selectedItem = tblOrdrs.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    for (String item : cmbItmCode.getItems()) {

                        if (item.equals(selectedItem.getItemCode())) {
                            cmbItmCode.getSelectionModel().select(item);
                        }

                    }
                    txtQty.setText(selectedItem.getQty() + "");
                    btnAdd.setText("Update");


                    int currentQty = selectedItem.getQty();
                    int avaQtyOnHand = Integer.parseInt(txtQtyOnHnd.getText());
                    int newQtyOnHand = avaQtyOnHand + currentQty;
                    String sql = "UPDATE Item set qtyOnHand='" + newQtyOnHand + "' WHERE itemCode='" + selectedItem.getItemCode() + "'";
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        int rowCount = preparedStatement.executeUpdate();
                        if (rowCount > 0) {
                            System.out.println("Updated Selected Qty On Hand");
                            String srcSql = "SELECT qtyOnHand from Item WHERE itemCode='" + selectedItem.getItemCode() + "'";
                            PreparedStatement preparedStatement1 = connection.prepareStatement(srcSql);
                            ResultSet resultSet = preparedStatement1.executeQuery();
                            resultSet.next();
                            txtQtyOnHnd.setText(resultSet.getString("qtyOnHand"));

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


            }
        });


    }


    public void navigate(ActionEvent actionEvent) throws IOException {

        for (OrderDetailTM item : tblOrdrs.getItems()) {
            if (item!=null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please place the Order or clear the table", ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    btnPlaceOrdr.requestFocus();
                    return;
                }

            }




        }
        if (actionEvent.getSource() instanceof Button) {
            Button btn = (Button) actionEvent.getSource();

            Parent root = null;

            switch (btn.getId()) {
                case "btnHome":
                    root = FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
                    break;

                case "btnManageCstmr":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageCustomer.fxml"));
                    break;

                case "btnManageItm":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ManageItem.fxml"));
                    break;

                case "btnViewOrdrs":
                    root = FXMLLoader.load(this.getClass().getResource("/view/ViewOrders.fxml"));
                    break;

            }


            if (root != null) {
                Scene scene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
                tt.setFromX(-scene.getWidth());
                tt.setToX(0);
                tt.play();


            }

        }

    }




    public void btnNwOnAction(ActionEvent actionEvent) {
        ArrayList<OrdersTM> orderArray = new ArrayList<>();
        String odId;
        String odDate;
        String custId;
        double tot;

        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM orders";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                odId = resultSet.getString(1);
                odDate = resultSet.getString(2);
                custId = resultSet.getString(3);
                tot = Double.parseDouble(resultSet.getString(4));
                orderArray.add(new OrdersTM(odId, odDate, custId, tot));


            }
            int arraySize = orderArray.size() + 1;
            String id = "";
            if (arraySize < 10) {

                id = "OD00" + arraySize;

            } else if (arraySize < 100) {
                id = "OD0" + arraySize;
            } else {
                id = "OD" + arraySize;
            }
            lblOrdrID.setText(id);
            cmbCstmrId.getSelectionModel().clearSelection();
            cmbItmCode.getSelectionModel().clearSelection();
            lblTotal.setText("");
            if (tblOrdrs.getItems()!=null){
                Alert place_the_order = new Alert(Alert.AlertType.ERROR, "Place the Order", ButtonType.OK);
                Optional<ButtonType> buttonType = place_the_order.showAndWait();
                if (buttonType.get()==ButtonType.OK){
                    return;
                }
            }
            txtCstmrName.clear();
            txtItemDesc.clear();
            txtQtyOnHnd.clear();
            txtUntPrz.clear();
            txtQty.clear();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        int DefaulyQtyOnHnad = Integer.parseInt(txtQtyOnHnd.getText());
        tblOrdrs.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblOrdrs.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("desc"));
        tblOrdrs.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrdrs.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrdrs.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        tblOrdrs.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

        OrderDetailTM selectedItem = tblOrdrs.getSelectionModel().getSelectedItem();
        int qtyOnHandUp = Integer.parseInt(txtQtyOnHnd.getText());
        int qtyUp = Integer.parseInt(txtQty.getText());

        if (btnAdd.getText().equals("Update")){
            if (txtQty.getText().matches("^[1-9]+[0]?[0]?[0]?$")){
                    qtyUp = Integer.parseInt(txtQty.getText());


            }else {
                Alert invalid_quanity = new Alert(Alert.AlertType.ERROR, "Invalid Quanity",ButtonType.OK);
                Optional<ButtonType> buttonType = invalid_quanity.showAndWait();
                if (buttonType.get()==ButtonType.OK){
                    txtQty.requestFocus();
                    return;
                }
            }
            if (qtyUp <= 0 || qtyUp > qtyOnHandUp ){
                notification("Invalid Quantity",txtQty);
                return;

            }else {
                setQtyOnHand();
                double upTot = calculateTotal(qtyUp,Double.parseDouble(txtUntPrz.getText()));
                selectedItem.setTotal(upTot);
                selectedItem.setQty(qtyUp);
                calculateMainTotal();
                tblOrdrs.refresh();
                return;
            }



        }

        Button btnDelete = new Button("Delete");

        String itemCode = cmbItmCode.getSelectionModel().getSelectedItem();
        int qty;
        if (txtQty.getText().matches("^[1-9]+[0]?[0]?[0]?$")){
            qty = Integer.parseInt(txtQty.getText());
        }else{
            notification("Invalid Quantity",txtQty);
            return;
        }
        int qtyOnHand = Integer.parseInt(txtQtyOnHnd.getText());
        double untPrize = Double.parseDouble(txtUntPrz.getText());
        if (qty <= 0 || qty > qtyOnHand ){


            notification("Invalid Quantity",txtQty);
            return;
        }
        setQtyOnHand();

        double total = calculateTotal(qty,Double.parseDouble(txtUntPrz.getText()));
        int tempQty = Integer.parseInt(txtQty.getText());
        int DefaultQtyOnHand2 = Integer.parseInt(txtQtyOnHnd.getText());
        int newQtyOnHand2 = DefaultQtyOnHand2-Integer.parseInt(txtQty.getText());

        for (OrderDetailTM item : tblOrdrs.getItems()) {

            if (item.getItemCode().equals(itemCode)){
                int itemQty = item.getQty();
                item.setQty((itemQty+tempQty));
                double newtot = calculateTotal(item.getQty(),Double.parseDouble(txtUntPrz.getText()));
                item.setTotal(newtot);
                calculateMainTotal();
                tblOrdrs.refresh();
                return;




            }

        }


                tblOrdrs.getItems().add(new OrderDetailTM(itemCode,txtItemDesc.getText(),qty,Double.parseDouble(txtUntPrz.getText()),total,btnDelete));
                tblOrdrs.refresh();

                calculateMainTotal();
                txtQty.clear();
                txtQty.requestFocus();



        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ObservableList<OrderDetailTM> DeletingItem = tblOrdrs.getItems();
                        OrderDetailTM selectedItem1 = tblOrdrs.getSelectionModel().getSelectedItem();
                         DeletingItem.remove(selectedItem1);
                         lblTotal.setText("");
                         btnAdd.setText("Save");
                         txtCstmrName.clear();
                         txtQtyOnHnd.clear();
                         txtUntPrz.clear();
                         txtQty.clear();
                         cmbCstmrId.getSelectionModel().clearSelection();
                         cmbItmCode.getSelectionModel().clearSelection();



                    }
                });

            }


            public void setQtyOnHand(){



                int currentQtyOnHand = Integer.parseInt(txtQtyOnHnd.getText());
                int qty = Integer.parseInt(txtQty.getText());
                int newQtyOnHand = currentQtyOnHand-qty;
                if (currentQtyOnHand>0){

                    String itemCdde = cmbItmCode.getSelectionModel().getSelectedItem();
                    Connection connection = DBConnection.getInstance().getConnection();
                    String sql = "UPDATE Item set qtyOnHand='"+newQtyOnHand+"' WHERE itemCode='"+itemCdde+"'";
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        int afftectRows = preparedStatement.executeUpdate();
                        if (afftectRows>0){
                            System.out.println("Qty On Hand updated");
                            String srchSql = "SELECT qtyOnHand from Item WHERE itemCode='"+itemCdde+"'";
                            PreparedStatement preparedStatement1 = connection.prepareStatement(srchSql);
                            ResultSet resultSet = preparedStatement1.executeQuery();
                            resultSet.next();
                            txtQtyOnHnd.setText(resultSet.getString("qtyOnHand"));


                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }else {

                    Alert stock_is_empty = new Alert(Alert.AlertType.ERROR, "Stock is Empty", ButtonType.OK);
                    Optional<ButtonType> buttonType = stock_is_empty.showAndWait();
                    if (buttonType.get()==ButtonType.OK){
                        return;

                    }
                }



            }





    public double calculateTotal(int qty,double unitPrice){
        double total = qty * unitPrice;

        return total;


    }
    public double calculateMainTotal(){
        double mainTotal = 0.00;
        for (OrderDetailTM item : tblOrdrs.getItems()) {
            mainTotal+=item.getTotal();


        }
        lblTotal.setText(String.valueOf(mainTotal));
        return mainTotal;


    }

    public void btnPlaceOrdrOnAction(ActionEvent actionEvent) throws SQLException {
        String itemCode;
        String orderId = lblOrdrID.getText();
        int qty;
        double unitPrice;
        String ordrDate = null;
        String customerId = cmbCstmrId.getSelectionModel().getSelectedItem();
        double total = Double.parseDouble(lblTotal.getText());
        Connection connection = DBConnection.getInstance().getConnection();
        for (OrderDetailTM item : tblOrdrs.getItems()) {
            if (item!=null) {
                itemCode = item.getItemCode();
                qty = item.getQty();
                unitPrice = item.getUnitPrice();
                orderId = lblOrdrID.getText();
                ordrDate = lblDate.getText();

                String sql2 = "INSERT INTO orders values(?,?,?,?)";

                try {
                    PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
                    preparedStatement1.setString(1, orderId);
                    preparedStatement1.setString(2, ordrDate);
                    preparedStatement1.setString(3, customerId);
                    preparedStatement1.setString(4, String.valueOf(total));

                    int rowCount = preparedStatement1.executeUpdate();


                    if (rowCount > 0) {

                        ObservableList<OrderDetailTM> items = tblOrdrs.getItems();
                        System.out.println(items);

                        Connection c = DBConnection.getInstance().getConnection();
                        PreparedStatement ptm = null;

                            ptm = connection.prepareStatement("insert into orderDetail values(?,?,?,?,?)");


                        for (OrderDetailTM item2 : items) {
                            ptm.setObject(1, item2.getItemCode());
                            ptm.setObject(2, orderId);
                            ptm.setObject(3, item2.getQty());
                            ptm.setObject(4, item2.getUnitPrice());
                            ptm.setObject(5, ordrDate);
                            ptm.executeUpdate();
                        }
                        break;


                    }

                } catch (Exception e){
                    System.out.println(e.getMessage()+"1");

                }

            }
                    }
        generateOrderID();


//                        String sql = "INSERT INTO orderDetail values (?,?,?,?,?)";
//                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//                        preparedStatement.setString(1,itemCode);
//                        preparedStatement.setString(2, orderId);
//                        preparedStatement.setString(3, String.valueOf(qty));
//                        preparedStatement.setString(4, String.valueOf(unitPrice));
//                        preparedStatement.setString(5,ordrDate);
//
//                        int rowCount2 = preparedStatement.executeUpdate();
//
//                        if (rowCount2>0){
//                            Alert order_places = new Alert(Alert.AlertType.INFORMATION, "Order Placed", ButtonType.OK);
//                            Optional<ButtonType> buttonType = order_places.showAndWait();
//                            if (buttonType.get()==ButtonType.OK){
//                                tblOrdrs.getItems().clear();
//
//
//
//
//
//                            }
//
//                        }
//
//
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//
//            }else {
//                return;
//            }
//
//
//        }*/




    }

    public void notification(String msg,JFXTextField id) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.OK) {
            id.requestFocus();

        }
    }
    public void generateOrderID(){
        ArrayList<OrdersTM> orderArray = new ArrayList<>();
        String odId;
        String odDate;
        String custId;
        double tot;

        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM orders";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                odId = resultSet.getString(1);
                odDate = resultSet.getString(2);
                custId = resultSet.getString(3);
                tot = Double.parseDouble(resultSet.getString(4));
                orderArray.add(new OrdersTM(odId, odDate, custId, tot));


            }
            int arraySize = orderArray.size() + 1;
            String id = "";
            if (arraySize < 10) {

                id = "OD00" + arraySize;

            } else if (arraySize < 100) {
                id = "OD0" + arraySize;
            } else {
                id = "OD" + arraySize;
            }
            lblOrdrID.setText(id);
           cmbCstmrId.getSelectionModel().clearSelection();
          cmbItmCode.getSelectionModel().clearSelection();
            lblTotal.setText("");
            txtCstmrName.clear();
            txtItemDesc.clear();
            txtQtyOnHnd.clear();
            txtUntPrz.clear();
            txtQty.clear();
            tblOrdrs.getItems().clear();



        } catch (SQLException e) {
            e.printStackTrace();
        }




    }


}
