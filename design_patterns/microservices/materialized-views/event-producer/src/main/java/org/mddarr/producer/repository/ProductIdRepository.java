package org.mddarr.producer.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.mddarr.producer.models.Product;
import org.mddarr.producer.models.ProductID;
import org.mddarr.products.AvroProduct;
import org.mddarr.products.AvroProductID;

import java.util.ArrayList;
import java.util.List;

public class ProductIdRepository {

    private static final String PRODUCTSID_TABLE_NAME = "productsid";

    private Session session;

    public ProductIdRepository(Session session) {
        this.session = session;
    }

    public List<ProductID> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM " + PRODUCTSID_TABLE_NAME);
        final String query = sb.toString();
        ResultSet rs = session.execute(query);
        List<ProductID> products = new ArrayList<>();
        for (Row r : rs.all()) {
            ProductID product = new ProductID();
            product.setProductid(r.getString("productid"));
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

    public static List<AvroProductID> mapAvroProducts(List<ProductID> products){
        List<AvroProductID> avroProducts = new ArrayList<>();
        for(ProductID product: products){
            AvroProductID avroProduct = AvroProductID.newBuilder()
                    .setProductid(product.getProductid())
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
