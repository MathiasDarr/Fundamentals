package org.mddarr.producer.models;

import java.util.List;


public class Order {
    String customerID;
    List<String> vendors;
    List<String> products;
    List<Long> quantities;

    public Order(){

    }

    public Order(String customerID, List<String> vendors, List<String> products, List<Long> quantities) {
        this.customerID = customerID;
        this.vendors = vendors;
        this.products = products;
        this.quantities = quantities;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public List<String> getVendors() {
        return vendors;
    }

    public void setVendors(List<String> vendors) {
        this.vendors = vendors;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<Long> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Long> quantities) {
        this.quantities = quantities;
    }
}
