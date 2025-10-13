package jonatan.store;

public class Product
{
    private String id;
    private String name;
    private String category;
    private double price;

    public Product(String id, String name, String category, double price)
    {
        this.name = name;
        this.id = id;
        this.category = category;
        this.price = price;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public double getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return String.format("%-30s | %-15s | %.2f $", name, category, price);
    }
}
