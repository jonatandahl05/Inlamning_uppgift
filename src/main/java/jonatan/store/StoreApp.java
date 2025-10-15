package jonatan.store;
import java.util.Map;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StoreApp {
    private static final Logger logger = LoggerFactory.getLogger(StoreApp.class);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StoreService store = new StoreService();

        //laddar in produkterna från metoden i Storeservice (ser mycket snyggare ut :))
        store.loadDefaultProducts();

        boolean running = true;
        while(running) {
            System.out.println("---> Välkommen <---");
            System.out.println("1. Visa alla produkter");
            System.out.println("2. Visa produkter per kategori");
            System.out.println("3. Visa topp 3 mest köpta prudkter");
            System.out.println("4. Köp produkter");
            System.out.println("5. Visa orderhistorik");
            System.out.println("6. Avsluta");
            System.out.println("Välj ett alternativ (1-4)");

            try{
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice){
                    case 1:
                        showAllProducts(store);
                        break;
                    case 2:
                        filterBycategory(scanner, store);
                        break;
                    case 3:
                        break;
                    case 4:
                        makePurchase(scanner, store);
                        break;
                    case 5:
                        showCustomerOrders(scanner, store);
                        break;
                    case 6:
                        running = false;
                        System.out.println("Progammet avslutas...");
                        logger.info("Progammet avslutas...");
                        break;
                    default:
                        System.out.println("Ogiltligt val, try again!");
                        logger.warn("Ogiltligt val, try again!");
                        break;
                }

            } catch (InputMismatchException e)
            {
                //loggar vad för fel
                logger.warn("Något typ av felaktiv inmatning, användaren skrev något som inte var en siffra.");

                System.out.println("Ogiltig inmatning! Skriv bara siffror mellan 1-6!");
                scanner.nextLine();

            }




        }


    }

    private static void showAllProducts(StoreService store)
    {
        System.out.println("\n---> Alla produkter <---");
        System.out.printf("%-30s | %-15s | %-6s%n", "Produkt", "Kategori", "Pris");
        System.out.println("-------------------------------------------------------------");

        store.getProducts().forEach(p ->
                System.out.printf("%-30s | %-15s | %.2f kr%n", p.getName(), p.getCategory(), p.getPrice()));
    }

    private static void makePurchase (Scanner scanner, StoreService store)
    {
        System.out.println("Ange kundens namn: ");
        String customerName = scanner.nextLine();

        Customer existing = store.findCustomerByName(customerName);
        Customer customer;

        if(existing == null) {
            customer = new Customer(customerName);
            store.addCustomer(customer);
            System.out.println("Ny kund skapad! Ditt kundnummer är: " +customer.getCustomerID());
        } else {
            customer = existing;
            System.out.println("Välkommen tillbaka, " + customer.getName() + "(Kundnummer: " + customer.getCustomerID());
        }



        System.out.println("---> Välj en kategori <---");

        Map<Integer, String> categories = store.getCategories();
        categories.forEach((num, name) -> System.out.println(num + ". " +name));

        System.out.println("Ditt val:");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine();

        String category = categories.get(categoryChoice);



        if (category == null)
        {
            System.out.println("Ogiltligt val, försök igen!");
            return;
        }
        List<Product> filteredProducts = store.filterByCategory(category);

        if (filteredProducts.isEmpty()) {
            System.out.println("Inga produkter hittades i kategorin: " + category);
        } else {
            System.out.println("Produkter i kategorin " + category + ":");
            for (int i = 0; i < filteredProducts.size(); i++)
            {
                System.out.printf("%d. %s%n", i + 1, filteredProducts.get(i));
            }
            System.out.println("Välj produktnummer att köpa (eller 0 för att avsluta)");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                System.out.println("Köpet avbrutet.");
                return;
            }

            boolean invalidChoice = choice < 1 || choice > filteredProducts.size();{
                if(invalidChoice) {
                    System.out.println("Ogiltigt val, välj ett nummer från listan");
                    return;
                }
            }

            Product selectedProduct = filteredProducts.get(choice - 1);

            String orderId = "O" + (store.getOrders().size() + 1);

            Order newOrder = new Order(orderId, List.of(selectedProduct), customerName);;
            store.addOrder(newOrder);

            System.out.println("Order skapad!");
            System.out.println("Ditt order ID är: " +orderId);
            System.out.println("Ditt kundnamn: " +customerName);
            System.out.println("Produkt köpt: " +selectedProduct.getName());
            System.out.println("Totala summa: " +selectedProduct.getPrice() + " $");

        }



    }


    private static void filterBycategory(Scanner scanner, StoreService store)
    {
        System.out.println("---> Välj en kategori <---");

        Map<Integer, String> categories = store.getCategories();
        categories.forEach((num, name) -> System.out.println(num + ". " +name));

        System.out.println("Ditt val: ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine();

        String category = categories.get(categoryChoice);


        if (category == null)
        {
            System.out.println("Ogiltligt val, försök igen!");
            return;
        }
        List<Product> filteredProducts = store.filterByCategory(category);

        if (filteredProducts.isEmpty()) {
            System.out.println("Inga produkter hittades i kategorin: " + category);
        } else {
            System.out.println("Produkter i kategorin " + category + ":");
            for (Product p : filteredProducts){
                System.out.println(p);
            }
        }

    }

    private static void showCustomerOrders (Scanner scanner, StoreService store){
        System.out.println("Ange kundens namn: ");
        String name = scanner.nextLine();

        List<Order> customerOrders = store.getOrdersByCustomer(name);

        if(customerOrders.isEmpty())
        {
            System.out.println("Inga ordar hittades för kunden: " + name);
            return;
        }

        System.out.println("---> Orderhistorik för: " +name + "<---");
        for (Order o : customerOrders){
            System.out.println("Order-id:" + o.getOrderID());
            System.out.println("Produkter:");
            for (Product p : o.getProducts()){
                System.out.println(" - " + p.getName() + " (" + p.getPrice() + "$");

            } double total = o.getProducts().stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
            System.out.printf("Totalt pris: %.2f $ %n", total);
            System.out.println("-----------");
        }

    }
}
