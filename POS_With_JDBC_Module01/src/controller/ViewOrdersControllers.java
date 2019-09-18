package controller;

import DB.DBConnection;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.SearchTM;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewOrdersControllers implements Initializable {


    public Button btnHome;
    public Button btnMngCustomer;
    public Button btnPlcOrder;
    public Button btnMngItems;
    public JFXTextField txtSrch;
    public TableView<SearchTM> tblSrcOrderss;
    public AnchorPane root;

    private Connection connection;
    private PreparedStatement ptsmForSearch;

    public void navigate(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof Button) {
            Button btn = (Button) actionEvent.getSource();

            Parent root = null;

            try {
                switch (btn.getId()) {
                    case "btnHome":
                        root = FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
                        break;

                    case "btnMngCustomer":
                        root = FXMLLoader.load(this.getClass().getResource("/view/ManageCustomer.fxml"));
                        break;

                    case "btnMngItems":
                        root = FXMLLoader.load(this.getClass().getResource("/view/ManageItem.fxml"));
                        break;

                    case "btnPlcOrder":
                        root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrder.fxml"));
                        break;

                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());

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

    public void loadOrders() throws SQLException {
        String search = txtSrch.getText();
        tblSrcOrderss.getItems().clear();

        ptsmForSearch.setString(1, "%" + search + "%");
        ptsmForSearch.setString(2, "%" + search + "%");
        ptsmForSearch.setString(3, "%" + search + "%");
        ptsmForSearch.setString(4, "%" + search + "%");
        ptsmForSearch.setString(5, "%" + search + "%");
        ResultSet resultSet = ptsmForSearch.executeQuery();


        ObservableList<SearchTM> items01 = tblSrcOrderss.getItems();
        while (resultSet.next()) {

            String ordrId = resultSet.getString("orderID");
            String ordrDate = resultSet.getString("orderDate");
            String custId = resultSet.getString("customerId");
            String name = resultSet.getString("name");
            String total = resultSet.getString("total");

            items01.add(new SearchTM(ordrId, ordrDate, custId, name, Double.parseDouble(total)));

        }


    }

    public void onKeyRelease(KeyEvent keyEvent) {
//        if (txtSrch.getText() != null) {
//            Connection connection = DBConnection.getInstance().getConnection();
//            String sql = "select orders.orderId, orders.orderDate, orders.customerId, customer.name, orders.total from orders left join customer on orders.customerId = customer.Id\n" +
//                    "where orders.orderId like ? or orders.orderDate like ? or orders.customerId like ? or customer.name like ? or orders.total like ?";
//            try {
//                PreparedStatement preparedStatement = connection.prepareStatement(sql);
//                preparedStatement.setString(1, "%" + txtSrch.getText() + "%");
//                preparedStatement.setString(2, "%" + txtSrch.getText() + "%");
//                preparedStatement.setString(3, "%" + txtSrch.getText() + "%");
//                preparedStatement.setString(4, "%" + txtSrch.getText() + "%");
//                preparedStatement.setString(5, "%" + txtSrch.getText() + "%");
//                ResultSet resultSet = preparedStatement.executeQuery();
//
//                tblSrcOrderss.getItems().clear();
//                ObservableList<SearchTM> items01 = tblSrcOrderss.getItems();
//                while (resultSet.next()) {
//
//                    String ordrId = resultSet.getString("orderID");
//                    String ordrDate = resultSet.getString("orderDate");
//                    String custId = resultSet.getString("customerId");
//                    String name = resultSet.getString("name");
//                    String total = resultSet.getString("total");
//
//                    items01.add(new SearchTM(ordrId, ordrDate, custId, name, Double.parseDouble(total)));
//
//
//                }
//                tblSrcOrderss.setItems(items01);
//                tblSrcOrderss.refresh();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//
//        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tblSrcOrderss.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderID"));
        tblSrcOrderss.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblSrcOrderss.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("custID"));
        tblSrcOrderss.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("custName"));
        tblSrcOrderss.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        String ordrId;
        String custid;
        String custName;
        String date;
        double total;

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ptsmForSearch = connection.prepareStatement("select orders.orderId, orders.orderDate, orders.customerId, customer.name, orders.total from orders left join customer on orders.customerId = customer.Id\n" +
                    "where orders.orderId like ? or orders.orderDate like ? or orders.customerId like ? or customer.name like ? or orders.total like ?");
            loadOrders();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtSrch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    loadOrders();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

//        Connection connection = DBConnection.getInstance().getConnection();
//        String sql = "SELECT orders.orderId, orders.customerId, orders.orderDate, orders.total, customer.name FROM orders INNER JOIN customer ON orders.customerId=customer.Id";
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()){
//                ordrId = resultSet.getString("orderId");
//                custid = resultSet.getString("customerId");
//                custName = resultSet.getString("name");
//                date = resultSet.getString("orderDate");
//                total = Double.parseDouble(resultSet.getString("total"));
//                tblSrcOrderss.getItems().add(new SearchTM(ordrId,custid,custName,date,total));
//
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }
}
