package jonatan.store;

public class Customer {
    private static int idCounter = 1;
    private final int customerId;
    private String name;

    public Customer(String name)
    {
        this.customerId = idCounter++;
        this.name = name;
    }

    public int getCustomerID()
    {
        return customerId;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return customerId + " - " +name;
    }
}
