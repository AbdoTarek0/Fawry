/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fawry;

import java.time.LocalDate;

/**
 *
 * @author ZaRZoR
 */
public class CombinedShippableExpirableProduct extends ExpirableProduct implements Shippable {
        private double weight;

        public CombinedShippableExpirableProduct(String name, double price, int quantity, LocalDate expirationDate, double weight) {
        super(name, price, quantity, expirationDate);
        this.weight = weight;
    }
        public double getWeight() {
        return weight;
    }
    
}
