package DB;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get()==ButtonType.OK){

                return;
            }
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/pos","root","ijse");


        } catch (Exception e) {
            throw new RuntimeException(e);

            }
        }



    public static DBConnection getInstance(){
        return dbConnection = ((dbConnection==null)? new DBConnection(): dbConnection);
    }

    public Connection getConnection(){

        return connection;
    }
}
