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
public class ExpirableProduct extends Product {
        private LocalDate expirationDate;
    
   public ExpirableProduct(String name, double price, int quantity, LocalDate expirationDate) {
        super(name, price, quantity);
        this.expirationDate = expirationDate;
    }
   public LocalDate getExpirationDate() {
        return expirationDate;
    }
   public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }
    
}
