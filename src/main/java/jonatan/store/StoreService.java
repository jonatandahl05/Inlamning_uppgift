package jonatan.store;

import java.util.*;
import java.util.stream.Collectors;

public class StoreService {

    //Använde mig att arraylist här eftersom det är enklet att lägga till, sortera och filtrera, samt behövs ingen unik nyckel bara en samling av produkterna och ordrarna.

    private List<Product> products = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private Map<String, List<Order>> orderHistory = new HashMap<>();

    //använder hashmap då jag får snabb åtkomst till kundernas orderhistorik till exempel via en unik nyckel (namnet)

    //Lägg till produkter
    public void addProduct (Product product)
    {
        products.add(product);
    }

    public void addOrder(Order order)
    {
        orders.add(order);
        orderHistory.computeIfAbsent(order.getCustomerName(), k -> new ArrayList<>()).add(order);
    }


    public List<Product> getProducts()
    {
        return products;
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public Map<String, List<Order>> getOrderHistory()
    {
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


// Standardprodukter till butiken, använde chatgpt för har verkligen inte den orken att skriva ner allt dehär själv :D

    public void loadDefaultProducts()
    {
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

//Här filtrerar vi med streams efter vald kategori samt sorterar efter pris
    public List<Product> filterByCategory(String category)
    {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .toList();

    }
//Här retunerar vi produkterna sortade efter priset (låg till hög)
    public List<Product> getProductSortedByPrice()
    {
        return  products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .toList();
    }
//Metod som används flera gånger med alla kategorier vi har att välja på

    public Map<Integer, String> getCategories()
    {
        Map<Integer, String> categories = new LinkedHashMap<>(); //Använder Linkedhashmap då jag vill att kategorierna ska visas i samma ordning varje gång i menyn.
        categories.put(1, "Energy drinks");
        categories.put(2, "Water");
        categories.put(3, "Juice");
        categories.put(4, "Coffee drinks");
        categories.put(5, "Soda");
        return categories;
    }

    public List<Order> getOrdersByCustomer(String customerName)
    {
        return orderHistory.getOrDefault(customerName, new ArrayList<>());
    }



    public List<Map.Entry<String, Long>> getTop3Products()
    {
        return orders.stream()
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .toList();
    }







}
