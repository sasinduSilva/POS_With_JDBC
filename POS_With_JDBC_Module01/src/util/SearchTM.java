package util;

public class SearchTM {
    String orderID;
    String custID;
    String custName;
    String date;
    double total;

    public SearchTM() {
    }

    public SearchTM(String orderID, String custID, String custName, String date, double total) {
        this.orderID = orderID;
        this.custID = custID;
        this.custName = custName;
        this.date = date;
        this.total = total;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SearchTM{" +
                "orderID='" + orderID + '\'' +
                ", custID='" + custID + '\'' +
                ", custName='" + custName + '\'' +
                ", date='" + date + '\'' +
                ", total=" + total +
                '}';
    }
}
