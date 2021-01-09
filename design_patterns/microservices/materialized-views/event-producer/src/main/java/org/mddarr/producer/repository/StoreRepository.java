package org.mddarr.producer.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.mddarr.producer.models.Product;
import org.mddarr.producer.models.Store;
import org.mddarr.products.AvroProduct;

import java.util.ArrayList;
import java.util.List;

public class StoreRepository {

    private static final String TABLE_NAME = "stores";


    private Session session;

    public StoreRepository(Session session) {
        this.session = session;
    }

    public List<Store> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM stores");
        final String query = sb.toString();
        ResultSet rs = session.execute(query);
        List<Store> stores = new ArrayList<>();
        for (Row r : rs.all()) {
            Store store = new Store(r.getString("storeid"), r.getInt("mean_customers_pery_day"), r.getInt("mean_neighberhood_income"));
            stores.add(store);
        }
        return stores;
    }

    public static List<AvroProduct> mapAvroProducts(List<Product> products){
        List<AvroProduct> avroProducts = new ArrayList<>();
        for(Product product: products){
            AvroProduct avroProduct = AvroProduct.newBuilder()
                    .setProduct(product.getProduct())
                    .setInventory(product.getInventory())
                    .setPrice(product.getPrice())
                    .setVendor(product.getVendor())
                    .build();
            avroProducts.add(avroProduct);
        }
        return avroProducts;
    }


}

