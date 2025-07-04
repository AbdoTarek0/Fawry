/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fawry;
import java.util.UUID;


/**
 *
 * @author ZaRZoR
 */
public class Product {
    public Product(String name, double price, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    private String id;
    private String name;
    private double price;
    private int quantity;
    
     public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void decreaseQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
        } else {
            throw new IllegalArgumentException("Not enough stock for " + name);
        }
    }

    public boolean isAvailable(int requestedQuantity) {
        return this.quantity >= requestedQuantity;
    }
}
