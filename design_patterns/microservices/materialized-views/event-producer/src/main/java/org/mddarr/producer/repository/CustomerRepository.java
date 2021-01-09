package org.mddarr.producer.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.mddarr.producer.models.Customer;
import org.mddarr.producer.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    private static final String TABLE_NAME = "customers";

    private Session session;

    public CustomerRepository(Session session) {
        this.session = session;
    }

    public List<Customer> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM customers");

        final String query = sb.toString();
        ResultSet rs = session.execute(query);

        List<Customer> customers = new ArrayList<>();

        for (Row r : rs.all()) {
            Customer product = new Customer(r.getString("customerid"), r.getInt("purchases_per_month"), r.getInt("average_purchase_amount"),r.getString("city"));
            customers.add(product);
        }
        return customers;
    }


}
