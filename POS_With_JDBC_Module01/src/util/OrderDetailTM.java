package util;

import javafx.scene.control.Button;

public class OrderDetailTM {
    String itemCode;
    String desc;
    int qty;
    double unitPrice;
    double total;
    Button btnDelete;



    public OrderDetailTM(String itemCode, String desc, int qty, double unitPrice, double total, Button btnDelete) {
        this.itemCode = itemCode;
        this.desc = desc;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.total = total;
        this.btnDelete = btnDelete;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }

    @Override
    public String toString() {
        return "OrderDetailTM{" +
                "itemCode='" + itemCode + '\'' +
                ", desc='" + desc + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                ", btnDelete=" + btnDelete +
                '}';
    }
}
