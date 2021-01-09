package org.mddarr.producer.models;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Store {
    private String storeid;
    private int mean_customers_pery_day;
    private int mean_neighberhood_income;

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public int getMean_customers_pery_day() {
        return mean_customers_pery_day;
    }

    public void setMean_customers_pery_day(int mean_customers_pery_day) {
        this.mean_customers_pery_day = mean_customers_pery_day;
    }

    public int getMean_neighberhood_income() {
        return mean_neighberhood_income;
    }

    public void setMean_neighberhood_income(int mean_neighberhood_income) {
        this.mean_neighberhood_income = mean_neighberhood_income;
    }
}
