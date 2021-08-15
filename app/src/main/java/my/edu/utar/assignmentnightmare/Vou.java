package my.edu.utar.assignmentnightmare;

public class Vou {
    String vouDesc;
    String vouName;

    public Vou(String vouName, String vouDesc) {
        this.vouName = vouName;
        this.vouDesc = vouDesc;
    }

    public String getVouName() {
        return vouName;
    }

    public void setVouName(String vouName) {
        this.vouName = vouName;
    }

    public String getVouDesc() {
        return vouDesc;
    }

    public void setVouDesc(String vouDesc) {
        this.vouDesc = vouDesc;
    }

}
