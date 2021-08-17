package my.edu.utar.assignmentnightmare;

import java.util.Date;
//Done by Low Wei Han
public class OrderModel {

    String productName;
    Double productPrice;
    String productImgUri;
    String sellername;
    int productQuantity;

    public OrderModel() {
    }

    public OrderModel(String productName, Double productPrice, String productImgUri, String sellername, int productQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImgUri = productImgUri;
        this.sellername = sellername;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImgUri() {
        return productImgUri;
    }

    public void setProductImgUri(String productImgUri) {
        this.productImgUri = productImgUri;
    }

    public String getsellername() {
        return sellername;
    }

    public void setsellername(String sellerName) {
        this.sellername = sellerName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}

