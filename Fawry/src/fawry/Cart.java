/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fawry;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author ZaRZoR
 */
public class Cart {
    private Map<Product, Integer> items; 

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) { 
        if (product == null) {
            System.out.println("Error: Cannot add null product to cart.");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }
        if (!product.isAvailable(quantity)) { // 
            System.out.println("Error: Sorry, only " + product.getQuantity() + " of '" + product.getName() + "' are currently available. Cannot add " + quantity + " to cart.");
            return;
        }
        items.put(product, items.getOrDefault(product, 0) + quantity);
        System.out.println(quantity + "x " + product.getName() + " added to cart.");
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public boolean isEmpty() { 
        return items.isEmpty();
    }

    public void clearCart() {
        items.clear();
        System.out.println("Cart has been cleared.");

    }
    
}
