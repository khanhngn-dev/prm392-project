package utils.https.types;

public class OrderProduct {
    private int id;
    private Product product;
    private int soldPrice;
    private int quantity;
    private int totalPrice;

    public OrderProduct(int id, Product product, int soldPrice, int quantity, int totalPrice) {
        this.id = id;
        this.product = product;
        this.soldPrice = soldPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderProduct() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(int soldPrice) {
        this.soldPrice = soldPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
