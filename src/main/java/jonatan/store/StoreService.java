package jonatan.store;

import java.util.*;
import java.util.stream.Collectors;

public class StoreService {

    private List<Product> products = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private Map<String, List<Order>> orderHistory = new HashMap<>();

    //Lägg till produkter
    public void addProduct (Product product) {
        products.add(product);
    }

    public void addOrder(Order order) {
        orders.add(order);
        orderHistory.computeIfAbsent(order.getCustomerName(), k -> new ArrayList<>()).add(order);
    }


    public List<Product> getProducts() {
        return products;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Map<String, List<Order>> getOrderHistory() {
        return orderHistory;
    }

    private List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer customer)
    {
        customers.add(customer);
    }

    public List<Customer> getCustomers()
    {
        return customers;
    }

    public Customer findCustomerByName(String name)
    {
        for(Customer c : customers) {
            if(c.getName().equalsIgnoreCase(name)){
                return c;
            }
        }
        return null;
    }




    public void loadDefaultProducts() {
        addProduct(new Product("Energy_1", "Monster - Mango Loco", "Energy drinks", 1.99));
        addProduct(new Product("Energy_2", "Red Bull - Apricot Edition", "Energy drinks", 1.79));
        addProduct(new Product("Energy_3", "Monster - Ultra White", "Energy drinks", 1.89));
        addProduct(new Product("Energy_4", "Reign - Razzle Berry", "Energy drinks", 2.19));
        addProduct(new Product("Energy_5", "Nocco - Caribbean", "Energy drinks", 2.49));
        addProduct(new Product("Energy_6", "Celsius - Tropical Vibe", "Energy drinks", 2.29));
        addProduct(new Product("Energy_7", "Monster - Pipeline Punch", "Energy drinks", 1.99));
        addProduct(new Product("Water_1", "Ramlösa - Citrus", "Water", 0.99));
        addProduct(new Product("Water_2", "Loka - Strawberry Ice Cream", "Water", 0.89));
        addProduct(new Product("Water_3", "Smartwater - Original", "Water", 1.49));
        addProduct(new Product("Juice_1", "Tropicana - Orange Juice", "Juice", 1.29));
        addProduct(new Product("Juice_2", "Brämhults - Apelsin", "Juice", 2.99));
        addProduct(new Product("Coffee_1", "Starbucks - Doubleshot Espresso", "Coffee drinks", 2.79));
        addProduct(new Product("Soda_1", "Coca Cola - Zero Sugar", "Soda", 1.09));
        addProduct(new Product("Soda_2", "Fanta - Exotic", "Soda", 1.09));
    }

    public List<Product> filterByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .toList();

    }

    public List<Product> getProductSortedByPrice() {
        return  products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .toList();
    }

    public Map<Integer, String> getCategories() {
        Map<Integer, String> categories = new LinkedHashMap<>();
        categories.put(1, "Energy drinks");
        categories.put(2, "Water");
        categories.put(3, "Juice");
        categories.put(4, "Coffee drinks");
        categories.put(5, "Soda");
        return categories;
    }




}
