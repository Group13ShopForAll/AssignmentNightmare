package my.edu.utar.assignmentnightmare;

public class Item {

    private int resourceImage;
    private String name;
    private double price;
    private String soldnum;

    public Item(int resourceImage, String name, double price,String soldnum) {
        this.resourceImage = resourceImage;
        this.name = name;
        this.price = price;
        this.soldnum = soldnum;
    }


    public int getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(int resourceImage) {
        this.resourceImage = resourceImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getSoldnum() {
        return soldnum;
    }

    public void setSoldnum(String soldnum) {
        this.soldnum = soldnum;
    }
}
