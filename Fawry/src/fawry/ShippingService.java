/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fawry;

import java.util.List;

/**
 *
 * @author ZaRZoR
 */
public class ShippingService {
    public void shipItems(List<ShippingServiceInterface> itemsToShip) { 
        if (itemsToShip.isEmpty()) {
            return;
        }
        System.out.println("\n** Shipment notice **");
        double totalPackageWeight = 0;
        for (ShippingServiceInterface item : itemsToShip) {
            System.out.println(String.format("%s (%.0fg)", item.getName(), item.getWeight())); 
            totalPackageWeight += item.getWeight();
        }
        System.out.println(String.format("Total consolidated package weight: %.1fkg", totalPackageWeight / 1000.0));
        System.out.println("*** Items dispatched for delivery! ***");
    }
}
