package my.edu.utar.assignmentnightmare;

// Done by Felix
public class CartProduct {


    String productName, productDesc;
    String productImgUri;
    double productPrice;
    int productStock;
    int productQuantity;

    public CartProduct() {
    }

    public CartProduct(String productName, String productDesc, String productImgUri, double productPrice, int productStock, int productQuantity) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.productImgUri = productImgUri;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImgUri() {
        return productImgUri;
    }

    public void setProductImgUri(String productImgUri) {
        this.productImgUri = productImgUri;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

}
