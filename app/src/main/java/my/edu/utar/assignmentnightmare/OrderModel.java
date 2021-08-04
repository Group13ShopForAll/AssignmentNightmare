package my.edu.utar.assignmentnightmare;

import java.util.Date;
//Done by Low Wei Han
public class OrderModel {

    String productName;
    Double productPrice;
    String buyerName;
    String buyerPhoneNumber;
    String buyerAddress;
    String sellerName;
    Date purchaseDate;

    OrderModel() {

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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public OrderModel(String productName, Double productPrice, String buyerName, String buyerPhoneNumber, String buyerAddress, String sellerName, Date purchaseDate) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.buyerName = buyerName;
        this.buyerPhoneNumber = buyerPhoneNumber;
        this.buyerAddress = buyerAddress;
        this.sellerName = sellerName;
        this.purchaseDate = purchaseDate;
    }




}

