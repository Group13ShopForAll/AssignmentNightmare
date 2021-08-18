package my.edu.utar.assignmentnightmare;

public class OrderListModel {


    String orderdate;
    String ordertime;

    public OrderListModel(String orderdate, String ordertime) {
        this.orderdate = orderdate;
        this.ordertime = ordertime;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }


    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

}
