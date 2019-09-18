package DB;

import util.OrdersTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersArray {
    public ArrayList<OrdersTM> orderArray = new ArrayList<>();


    public OrdersArray() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * from orders";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String ordrId = resultSet.getString(1);
            String ordrDate = resultSet.getString(2);
            String custId = resultSet.getString(3);
            double total = Double.parseDouble(resultSet.getString(4));

            orderArray.add(new OrdersTM(ordrId, ordrDate, custId, total));

        }
    }

}
