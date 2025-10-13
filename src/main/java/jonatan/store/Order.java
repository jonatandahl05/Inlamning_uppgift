package jonatan.store;

import java.util.List;

public class Order {

    private String orderID;
    private List<Product> products; // enkelt att filtrera med streams
    private String customerName;

    public Order(String orderID, List<Product> products, String customerName)
    {
        this.orderID = orderID;
        this.products = products;
        this.customerName = customerName;
    }

    public String getOrderID()
    {
        return orderID;
    }

    public void setOrderID(String orderID)
    {
        this.orderID = orderID;
    }

    public List<Product> getProducts()
    {
        return products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
}
