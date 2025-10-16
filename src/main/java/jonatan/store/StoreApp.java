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
            //huvudmenyn för användaren
            System.out.println("---> Välkommen <---");
            System.out.println("1. Visa alla produkter");
            System.out.println("2. Visa produkter per kategori");
            System.out.println("3. Visa topp 3 mest köpta prudkter");
            System.out.println("4. Köp produkter");
            System.out.println("5. Visa orderhistorik");
            System.out.println("6. Avsluta");
            System.out.println("Välj ett alternativ (1-6)");

            try
            {
                //Tar emot användares val
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice)
                {
                    case 1:
                        showAllProducts(store);
                        break;
                    case 2:
                        filterBycategory(scanner, store);
                        break;
                    case 3:
                        showTop3MostBoughtProducts(store);
                        break;
                    case 4:
                        makePurchase(scanner, store);
                        break;
                    case 5:
                        showCustomerOrders(scanner, store);
                        break;
                    case 6:
                        running = false;
                        System.out.println("Programmet avslutas...");
                        logger.info("Programmet avslutas av användaren");
                        break;
                    default:
                        System.out.println("Ogiltligt val, try again!");
                        logger.warn("Användaren skrev ett ogiltligt val");
                        break;
                }

            } catch (InputMismatchException e)
            {
                //Fångar fel om användaren skriver bokstäver istället för siffror t.ex.
                logger.warn("Något typ av felaktiv inmatning, användaren skrev något som inte var en siffra.");

                System.out.println("Ogiltig inmatning! Skriv bara siffror mellan 1-6!");
                scanner.nextLine();
            }
        }
    }

   //Hämtar och skriver ut produkter i butiken på ett väldigt mycket snyggare sätt :D
    private static void showAllProducts(StoreService store)
    {
        System.out.println("\n---> Alla produkter <---");
        System.out.printf("%-30s | %-15s | %-6s%n", "Produkt", "Kategori", "Pris");
        System.out.println("-------------------------------------------------------------");

        store.getProducts().forEach(p ->
                System.out.printf("%-30s | %-15s | %.2f kr%n", p.getName(), p.getCategory(), p.getPrice()));
    }

    //Hanterar användarens köp
    private static void makePurchase (Scanner scanner, StoreService store)
    {
        System.out.println("Ange kundens namn: ");
        String customerName = scanner.nextLine();

        //kollar om användaren redan finns i listan
        Customer existing = store.findCustomerByName(customerName);
        Customer customer;

        if(existing == null)
        {
            customer = new Customer(customerName);
            store.addCustomer(customer);
            System.out.println("Ny kund skapad! Ditt kundnummer är: " +customer.getCustomerID());
        } else
        {
            customer = existing;
            System.out.println("Välkommen tillbaka, " + customer.getName() + " (Kundnummer: " + customer.getCustomerID() + ")");
        }

        System.out.println("---> Välj en kategori <---");
        //Hämtar kategorierna (LinkedHashMap för behålla ordningen och används som en metod också när man vill bara se produkterna sorterade i kategorierna)

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

        //Filtrerar produkterna i vald kategori med hjälp av streams
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

            Order newOrder = new Order(orderId, List.of(selectedProduct), customerName);
            store.addOrder(newOrder);

            System.out.println("Order skapad!");
            System.out.println("Ditt order ID är: " +orderId);
            System.out.println("Ditt kundnamn: " +customerName);
            System.out.println("Produkt köpt: " +selectedProduct.getName());
            System.out.println("Totala summa: " +selectedProduct.getPrice() + " $");
        }
    }

//Filtrerar produkter baserat på vald kategori, här används en Map (Integer -> String) för att koppla menyval till kategorinamn.

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

        if (filteredProducts.isEmpty())
        {
            System.out.println("Inga produkter hittades i kategorin: " + category);
        } else {
            System.out.println("Produkter i kategorin " + category + ":");
            for (Product p : filteredProducts){
                System.out.println(p);
            }
        }
    }
    //Visar användarens orderhistorik samt beräknar den totala summan användaren har spenderat
    private static void showCustomerOrders(Scanner scanner, StoreService store) {
        System.out.println("Ange kundens namn: ");
        String name = scanner.nextLine();

        List<Order> customerOrders = store.getOrdersByCustomer(name);

        if (customerOrders.isEmpty()) {
            System.out.println("Inga ordrar hittades för kunden: " + name);
            return;
        }

        System.out.println("---> Orderhistorik för: " + name + " <---");
        double totalSpent = 0; // här håller vi reda på kundens totala spenderade belopp

        for (Order o : customerOrders) {
            System.out.println("Order-id: " + o.getOrderID());
            System.out.println("Produkter:");
            for (Product p : o.getProducts()) {
                System.out.println(" - " + p.getName() + " (" + p.getPrice() + " $)");
            }

            // Beräknar summan för den enskilda ordern
            double orderTotal = o.getProducts().stream()
                    .mapToDouble(Product::getPrice)
                    .sum();

            // Skriver ut totalsumman för den ordern
            System.out.printf("Totalt pris för order: %.2f $%n", orderTotal);
            System.out.println("-----------");

            // Lägger till orderns total i den totala kundsumman
            totalSpent += orderTotal;
        }

        // Efter loopen – skriv ut kundens totala summa snyggt =)
        System.out.printf("Totalt spenderat av %s: %.2f $%n", name, totalSpent);
    }

    //Visar de tre mest köpta produkterna totalt

    private static void showTop3MostBoughtProducts(StoreService store){
        System.out.println("---> Topp 3 mest köpta produkter <---");

        try
        {
            if(store.getOrders().isEmpty()) {
                System.out.println("Inga köp har gjorts än");
                logger.info("Försök att visa topp 3 ordrar utan några ordar");
                return;
            }

            List<Map.Entry<String, Long>> top3 = store.getTop3Products();

            if(top3.isEmpty())
            {
                System.out.println("Inga produkter har köpts ännu");
                logger.info("Topp 3 retunerade en tom lista, inga produkter är köpta ännu.");
                return;
            }

            System.out.println("De tre mest köpa produkterna");
            for(Map.Entry<String, Long> entry : top3) {
                System.out.printf("%s - %d köp%n", entry.getKey(), entry.getValue());
                logger.info("Topp 3 visade utan problem :)");
            }

        } catch (Exception e) {
            System.out.println("Ett oväntat fel uppstod när topp 3 skulle visas");
            logger.error("Fel när Topp 3 mest köpa produkter skulle visas");
        }
    }
}
