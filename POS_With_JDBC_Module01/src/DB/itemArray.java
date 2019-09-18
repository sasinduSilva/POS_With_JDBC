package DB;

import util.CustoemrTM;
import util.itemTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class itemArray {


    public ArrayList<itemTM> itemArray = new ArrayList<>();


    public itemArray() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * from Item";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String itemCode = resultSet.getString(1);
            String desc = resultSet.getString(2);
            String qtyOnhand = resultSet.getString(3);
            String untPrice = resultSet.getString(4);

            itemArray.add(new itemTM(itemCode, desc, Integer.parseInt(qtyOnhand), Double.parseDouble(untPrice)));

        }
    }
}
