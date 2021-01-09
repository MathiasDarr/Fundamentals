package org.mddarr.producer.services;


import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.products.AvroProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    public static List<AvroProduct> getProductsFromCassandra(){
        List<AvroProduct> avroProducts = new ArrayList<>();
        return avroProducts;
    }

    public static List<AvroProduct> getProductsFromDynamo(){
        List<AvroProduct> avroProducts = new ArrayList<>();
        return avroProducts;
    }

    public static List<AvroOrder> getOrdersFromDynamo(){
        List<AvroOrder> avroOrders = new ArrayList<>();
        return avroOrders;
    }

    public static List<AvroOrder> getOrdersFromCassandra(){
        List<AvroOrder> avroOrders = new ArrayList<>();
        return avroOrders;
    }

}
