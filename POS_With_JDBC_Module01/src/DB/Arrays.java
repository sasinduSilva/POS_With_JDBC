package DB;

import util.CustoemrTM;
import util.itemTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Arrays {

    public  ArrayList<CustoemrTM> custArray = new ArrayList<>();
    public ArrayList<itemTM>  itemArray = new ArrayList<>();



    public Arrays() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * from customer";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String contactNo = resultSet.getString(4);

            custArray.add(new CustoemrTM(id,name,address,contactNo));

        }

    }





}
