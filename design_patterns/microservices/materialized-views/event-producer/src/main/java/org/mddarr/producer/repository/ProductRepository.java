package org.mddarr.producer.repository;

import java.util.ArrayList;
import java.util.List;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.mddarr.producer.models.Product;
import org.mddarr.products.AvroProduct;

public class ProductRepository {

    private static final String TABLE_NAME = "products";

    private static final String TABLE_NAME_BY_TITLE = TABLE_NAME + "ByTitle";

    private Session session;

    public ProductRepository(Session session) {
        this.session = session;
    }

    public List<Product> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM products");
        final String query = sb.toString();
        ResultSet rs = session.execute(query);
        List<Product> products = new ArrayList<>();
        for (Row r : rs.all()) {
            Product product = new Product();
            product.setPopularity(r.getInt("popularity"));
            product.setVendor(r.getString("vendor"));
            product.setCategory(r.getString("category"));
            product.setPrice(r.getFloat("price"));
            product.setInventory(r.getLong("inventory"));
            product.setProduct(r.getString("name"));
            products.add(product);
        }
        return products;
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
