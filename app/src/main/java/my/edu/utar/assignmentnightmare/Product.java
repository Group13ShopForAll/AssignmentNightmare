package my.edu.utar.assignmentnightmare;

// Done by Jiun Lin
public class Product {

    String productName, productDesc;
    String productImgUri;
    double productPrice;
    int productStock;

    public Product() {
    }

    public Product(String productName, String productDesc, String productImgUri, double productPrice, int productStock) {
        this.productName = productName;
        this.productDesc = productDesc;
        this.productImgUri = productImgUri;
        this.productPrice = productPrice;
        this.productStock = productStock;
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
}
