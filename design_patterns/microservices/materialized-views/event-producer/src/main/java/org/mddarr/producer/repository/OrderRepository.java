package org.mddarr.producer.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.OrderState;
import org.mddarr.producer.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepository {

    private static final String TABLE_NAME = "orders";

    private Session session;

    public OrderRepository(Session session) {
        this.session = session;
    }

    public List<Order> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM orders;");

        final String query = sb.toString();
        ResultSet rs = session.execute(query);
        String customerID;
        List<String> vendors;
        List<String> products;
        List<Long> quantities;
        List<Order> orders = new ArrayList<>();

        for (Row r : rs.all()) {
            Order product = new Order(r.getString("customerID"), r.getList("vendors", String.class),r.getList("products", String.class), r.getList("quantities", Long.class));
            orders.add(product);
        }
        return orders;
    }

    public static List<AvroOrder> mapAvroOrders(List<Order> orders){
        List<AvroOrder> avroOrders = new ArrayList<>();
        for(Order order: orders){
            AvroOrder avroOrder = AvroOrder.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setCustomerId(order.getCustomerID())
                    .setVendors(order.getVendors())
                    .setPrice(12.1)
                    .setState(OrderState.PENDING)
                    .setProducts(order.getProducts())
                    .setQuantites(order.getQuantities())
                    .build();
            avroOrders.add(avroOrder);
        }
        return avroOrders;
    }


}
