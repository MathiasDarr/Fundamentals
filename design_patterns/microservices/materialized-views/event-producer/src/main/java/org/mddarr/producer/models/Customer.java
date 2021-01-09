package org.mddarr.producer.models;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    String customerid;

    int purchases_per_month;
    int average_purchase_amount;
    String city;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public int getPurchases_per_month() {
        return purchases_per_month;
    }

    public void setPurchases_per_month(int purchases_per_month) {
        this.purchases_per_month = purchases_per_month;
    }

    public int getAverage_purchase_amount() {
        return average_purchase_amount;
    }

    public void setAverage_purchase_amount(int average_purchase_amount) {
        this.average_purchase_amount = average_purchase_amount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

