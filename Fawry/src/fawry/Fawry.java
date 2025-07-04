/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fawry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fawry {
    private Map<String, Product> productCatalog;
    private ShippingService shippingService;
    private double SHIPPING_RATE_PER_KG = 10.0;

    public Fawry() {
        this.productCatalog = new HashMap<>();
        this.shippingService = new ShippingService();
        Product tv = new ShippableProduct("TV", 1000.0, 5, 15000.0);
        Product mobile = new ShippableProduct("Mobile", 500.0, 10, 300.0);
        ExpirableProduct scratchCard = new ExpirableProduct("Mobile Scratch Card", 50.0, 20, LocalDate.now().plusYears(1));
        CombinedShippableExpirableProduct cheese = new CombinedShippableExpirableProduct("Cheese", 100.0, 8, LocalDate.now().plusMonths(2), 200.0);
        CombinedShippableExpirableProduct biscuits = new CombinedShippableExpirableProduct("Biscuits", 75.0, 12, LocalDate.now().plusMonths(1), 700.0);

        productCatalog.put(tv.getId(), tv);
        productCatalog.put(mobile.getId(), mobile);
        productCatalog.put(scratchCard.getId(), scratchCard);
        productCatalog.put(cheese.getId(), cheese);
        productCatalog.put(biscuits.getId(), biscuits);
    }

    public Product getProductById(String id) {
        return productCatalog.get(id);
    }

    public void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Error: Cart is empty. Please add items before checking out.");
            return;
        }

        double subtotal = 0;
        double totalShippingWeight = 0;
        List<ShippingServiceInterface> itemsToShip = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantityInCart = entry.getValue();

            Product currentProduct = productCatalog.get(product.getId());

            if (currentProduct == null) {
                System.out.println("Error: Product " + product.getName() + " not found in catalog.");
                return;
            }

            if (!currentProduct.isAvailable(quantityInCart)) {
                System.out.println("Error: " + currentProduct.getName() + " is out of stock or requested quantity (" + quantityInCart + ") is more than available (" + currentProduct.getQuantity() + ").");
                return;
            }

            if (currentProduct instanceof ExpirableProduct) {
                ExpirableProduct expirableProduct = (ExpirableProduct) currentProduct;
                if (expirableProduct.isExpired()) {
                    System.out.println("Error: " + expirableProduct.getName() + " has expired.");
                    return;
                }
            }

            subtotal += currentProduct.getPrice() * quantityInCart;

            if (currentProduct instanceof Shippable) {
                Shippable shippableItem = (Shippable) currentProduct;
                totalShippingWeight += shippableItem.getWeight() * quantityInCart;
                itemsToShip.add(new ShippingServiceInterface() {
                    @Override
                    public String getName() {
                        return quantityInCart + "x " + shippableItem.getName();
                    }

                    @Override
                    public double getWeight() {
                        return shippableItem.getWeight() * quantityInCart;
                    }
                });
            }
        }

        double shippingFees = (totalShippingWeight / 1000.0) * SHIPPING_RATE_PER_KG;
        double paidAmount = subtotal + shippingFees;

        if (customer.getBalance() < paidAmount) {
            System.out.println("Error: Customer's balance is insufficient. Required: " + paidAmount + ", Available: " + customer.getBalance());
            return;
        }

        customer.debit(paidAmount);

        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = productCatalog.get(entry.getKey().getId());
            product.decreaseQuantity(entry.getValue());
        }

        if (!itemsToShip.isEmpty()) {
            shippingService.shipItems(itemsToShip);
        }
        System.out.println("-------------------------------------\n");
        System.out.println("** Checkout receipt **");
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantityInCart = entry.getValue();
            System.out.println(String.format("%dx %s %.2f", quantityInCart, product.getName(), product.getPrice() * quantityInCart));
        }
        System.out.println(String.format("Subtotal %.2f", subtotal));
        System.out.println(String.format("Shipping %.2f", shippingFees));
        System.out.println(String.format("Amount %.2f", paidAmount));
        System.out.println(String.format("Customer current balance after payment: %.2f", customer.getBalance()));
        System.out.println("Thank you For Shipping.");

        cart.clearCart();
        System.out.println("-------------------------------------\n");
    }

    public static void main(String[] args) {
        Fawry system = new Fawry();

        System.out.println("--- Use Case 1: Successful Checkout ---");
        Customer customer1 = new Customer("Zeyad", 1000.0);
        Cart cart1 = new Cart();

        system = new Fawry();
        Product cheese = system.productCatalog.values().stream().filter(p -> p.getName().equals("Cheese")).findFirst().get();
        Product biscuits = system.productCatalog.values().stream().filter(p -> p.getName().equals("Biscuits")).findFirst().get();
        Product tv = system.productCatalog.values().stream().filter(p -> p.getName().equals("TV")).findFirst().get();
        Product scratchCard = system.productCatalog.values().stream().filter(p -> p.getName().equals("Mobile Scratch Card")).findFirst().get();


        cart1.addProduct(cheese, 2);
        cart1.addProduct(biscuits, 1);

        System.out.println(customer1.getName()+" balance before checkout: " + customer1.getBalance()+"$");
        system.checkout(customer1, cart1);
        System.out.println(customer1.getName()+" balance after checkout: " + customer1.getBalance()+"$");
        System.out.println("-------------------------------------\n");

        System.out.println("--- Use Case 2: Empty Cart ---");
        Customer customer2 = new Customer("Abdelrahman", 500.0);
        Cart cart2 = new Cart();
        system.checkout(customer2, cart2);
        System.out.println("-------------------------------------\n");

        System.out.println("--- Use Case 3: Insufficient Balance ---");
        Customer customer3 = new Customer("Ahmed", 50.0);
        Cart cart3 = new Cart();
        cart3.addProduct(tv, 1);
        System.out.println(customer3.getName()+" balance before checkout: " + customer3.getBalance()+"$");
        system.checkout(customer3, cart3);
        System.out.println(customer3.getName()+" balance after checkout: " + customer3.getBalance()+"$");
        System.out.println("-------------------------------------\n");

        System.out.println("--- Use Case 4: Product Out of Stock ---");
        Customer customer4 = new Customer("MOhamed", 500.0);
        Cart cart4 = new Cart();
        Product mobileFromSystem = system.getProductById(system.productCatalog.values().stream().filter(p -> p.getName().equals("Mobile")).findFirst().get().getId());
        int initialMobileQuantity = mobileFromSystem.getQuantity();
        System.out.println("Initial Mobile quantity: " + initialMobileQuantity);
        cart4.addProduct(mobileFromSystem, initialMobileQuantity + 1);
        System.out.println("Mobile stock after checkout attempt: " + mobileFromSystem.getQuantity());
        System.out.println("-------------------------------------\n");

        System.out.println("--- Use Case 5: Expired Product ---");
        Customer customer5 = new Customer("Mahmode", 500.0);
        Cart cart5 = new Cart();
        ExpirableProduct expiredCheese = new CombinedShippableExpirableProduct("Expired Cheese", 80.0, 5, LocalDate.now().minusDays(10), 150.0);
        system.productCatalog.put(expiredCheese.getId(), expiredCheese);

        cart5.addProduct(expiredCheese, 1);
        system.checkout(customer5, cart5);
        System.out.println("-------------------------------------\n");

        System.out.println("--- Use Case 6: Mixed Cart ---");
        Customer customer6 = new Customer("Raghad", 2000.0);
        Cart cart6 = new Cart();
        cart6.addProduct(tv, 1);
        cart6.addProduct(scratchCard, 2);
        System.out.println(customer6.getName()+" balance before checkout: " + customer6.getBalance()+"$");
        system.checkout(customer6, cart6);
        System.out.println(customer6.getName()+" balance after checkout: " + customer6.getBalance()+"$");
        System.out.println("-------------------------------------\n");

        System.out.println("--- Use Case 7: Add more than available product quantity ---");
        Customer customer7 = new Customer("Tarek", 1000.0);
        Cart cart7 = new Cart();
        Product someProduct = system.getProductById(system.productCatalog.values().stream().filter(p -> p.getName().equals("Mobile")).findFirst().get().getId());
         initialMobileQuantity = someProduct.getQuantity();
        System.out.println("Initial Mobile quantity: " + initialMobileQuantity);
        cart7.addProduct(someProduct, initialMobileQuantity + 1);
        System.out.println("-------------------------------------\n");
    }
}
