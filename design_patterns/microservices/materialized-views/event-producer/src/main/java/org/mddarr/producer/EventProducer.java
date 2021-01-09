package org.mddarr.producer;

import com.datastax.driver.core.Session;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mddarr.customers.AvroCustomer;

import org.mddarr.producer.models.*;
import org.mddarr.products.AvroPurchaseCount;
import org.mddarr.products.AvroPurchaseEvent;



import org.mddarr.orders.event.dto.AvroOrder;
import org.mddarr.orders.event.dto.OrderState;
import org.mddarr.producer.config.CassandraConnector;
import org.mddarr.producer.kafka.templates.KafkaGenericTemplate;
import org.mddarr.producer.repository.*;


import org.mddarr.producer.runnable.PurchaseEventProducerThread;
import org.mddarr.products.AvroProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventProducer {

    private static final Logger log = LoggerFactory.getLogger(EventProducer.class);

    private ExecutorService executor;
    private CountDownLatch latch;
//    private PurchaseEventProducerThread purchaseEventProducerThread;
    private List<PurchaseEventProducerThread>purchaseEventProducerThreads;

    public static void main(String[] args) throws Exception {
        populateSingleOrder();
        populateSinglePurchaseEvent();

        // EventProducer app = new EventProducer(args);
        //app.start();
    }


    public static void populateSinglePurchaseEvent(){
        KafkaGenericTemplate<AvroPurchaseEvent> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroPurchaseEvent> ordersKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        ordersKafkaTemplate.setDefaultTopic(Constants.PURCHASE_EVENT_TOPIC);
        AvroPurchaseEvent avroPurchaseEvent = AvroPurchaseEvent.newBuilder()
                .setProductid("product1")
                .build();
        System.out.println("i sent make a purchase event " + avroPurchaseEvent);
        ordersKafkaTemplate.sendDefault(avroPurchaseEvent.getProductid(), avroPurchaseEvent);
    }

    public static void populateSingleOrder() throws Exception {
        /*
        This method populates a single hardcoded order to kafka
         */
        KafkaGenericTemplate<AvroOrder> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroOrder> ordersKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        ordersKafkaTemplate.setDefaultTopic(Constants.ORDERS_TOPIC);

        List<String> vendors = new ArrayList(Arrays.asList("vendor1"));
        List<String> products = new ArrayList(Arrays.asList("product1"));
        List<Long> quantities = new ArrayList(Arrays.asList(1L));

        AvroOrder avroOrder = AvroOrder.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setState(OrderState.PENDING)
                .setVendors(vendors)
                .setQuantites(quantities)
                .setProducts(products)
                .setPrice(120)
                .setCustomerId("Charles Goodwin")
                .build();
        System.out.println("i sent an order " + avroOrder);
        ordersKafkaTemplate.sendDefault(avroOrder);
    }

    public static void populateOrder() throws Exception {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        OrderRepository br = new OrderRepository(session);

        KafkaGenericTemplate<AvroOrder> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroOrder> ordersKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        ordersKafkaTemplate.setDefaultTopic(Constants.ORDERS_TOPIC);
        connector.close();
    }


    public static void populateProductIDPurchaseEvents() throws InterruptedException {
        /*
        This method generates simulates purchases by randomly selecting products weighted by their popularity.
        Publishes these purchase events to kafka with avro serialization as AvroPurchaseEvents.
         */
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        ProductIdRepository productRepository = new ProductIdRepository(session);

        List<ProductID> products = productRepository.selectAll();

        WeightedRandomBag<ProductID> weighted_product_purchases = new WeightedRandomBag<>();

        for(ProductID product: products){
            weighted_product_purchases.addEntry(product,product.getPopularity());
        }

        KafkaGenericTemplate<AvroPurchaseEvent> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroPurchaseEvent> purchaseEventKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        purchaseEventKafkaTemplate.setDefaultTopic(Constants.PURCHASE_EVENT_TOPIC);

        while(true){
            ProductID product = weighted_product_purchases.getRandom();
            System.out.println("THE Product SELECTED WAS " + product);

            String productid = product.getProductid();
            AvroPurchaseEvent avroPurchaseEvent = AvroPurchaseEvent
                    .newBuilder()
                    .setProductid(product.getProductid())
                    .build();

            // Concatenate the vendor & the product as the key.. The value will contain the same information ..
            purchaseEventKafkaTemplate.sendDefault(avroPurchaseEvent.getProductid(),avroPurchaseEvent);

            Thread.sleep(500);
        }
    }

    public static void populateProductPurchaseCount() throws InterruptedException {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        ProductIdRepository productRepository = new ProductIdRepository(session);

        List<ProductID> products = productRepository.selectAll();

        WeightedRandomBag<ProductID> weighted_product_purchases = new WeightedRandomBag<>();

        Map<String, Integer> productPurchaseCounts = new HashMap<>();

        for(ProductID product: products){
            String produtid = product.getProductid();
            productPurchaseCounts.put(produtid, 0);
            weighted_product_purchases.addEntry(product,product.getPopularity());
        }

        KafkaGenericTemplate<AvroPurchaseCount> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroPurchaseCount> productKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        productKafkaTemplate.setDefaultTopic(Constants.PURCHASE_COUNTS_TOPIC);

        while(true){
            ProductID product = weighted_product_purchases.getRandom();

            String productid = product.getProductid();
            Integer count = productPurchaseCounts.get(productid)+1;
            System.out.println("THE Product SELECTED WAS " + product + " and it has been purchased " + count);

            AvroPurchaseCount avroPurchaseCount = AvroPurchaseCount.newBuilder()
                    .setCount(count)
                    .setProductId(productid)
                    .build();

            productPurchaseCounts.replace(productid, count);
            // Concatenate the vendor & the product as the key.. The value will contain the same information ..
            productKafkaTemplate.sendDefault(avroPurchaseCount);

            Thread.sleep(100);
        }
    }


    public static void populatePurchaseEvents() throws InterruptedException {
        /*
        This method generates simulates purchases by randomly selecting products weighted by their popularity.
        Publishes these purchase events to kafka with avro serialization as AvroPurchaseEvents.
         */
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        ProductRepository productRepository = new ProductRepository(session);

        List<Product> products = productRepository.selectAll();

        WeightedRandomBag<Product> weighted_product_purchases = new WeightedRandomBag<>();

        for(Product product: products){
            weighted_product_purchases.addEntry(product,product.getPopularity());
        }

        KafkaGenericTemplate<AvroPurchaseEvent> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroPurchaseEvent> purchaseEventKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        purchaseEventKafkaTemplate.setDefaultTopic(Constants.PURCHASE_EVENT_TOPIC);

        while(true){
            Product product = weighted_product_purchases.getRandom();
            System.out.println("THE Product SELECTED WAS " + product);

            AvroPurchaseEvent avroPurchaseEvent = AvroPurchaseEvent
                    .newBuilder()
                    .setProductid(product.getProduct())
                    .build();

            // Concatenate the vendor & the product as the key.. The value will contain the same information ..
            purchaseEventKafkaTemplate.sendDefault(avroPurchaseEvent.getProductid(),avroPurchaseEvent);

            Thread.sleep(500);
        }
    }




    private EventProducer(String[] arguments){
        latch = new CountDownLatch(2);
        executor = Executors.newFixedThreadPool(2);
//        purchaseEventProducerThread = new PurchaseEventProducerThread(latch);
    }

    public void start(){
        /*
        This method starts a multithreaded application.
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!executor.isShutdown()) {
                log.info("Shutdown requested");
                shutdown();
            }
        }));

        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        log.info("Application started!");
        StoreRepository storeRepository = new StoreRepository(session);

        List<Store> stores = storeRepository.selectAll();
        stores.forEach(store->{
            System.out.println("The store is " + store);
        });

        purchaseEventProducerThreads = new ArrayList<>();
        stores.forEach(store->{
            PurchaseEventProducerThread producerThread = new PurchaseEventProducerThread(latch, store);
            executor.submit(producerThread);
            purchaseEventProducerThreads.add(producerThread);
        });

        log.info("Stuff submit");
        try {
            log.info("Latch await");
            latch.await();
            log.info("Threads completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            shutdown();
            log.info("Application closed succesfully");
        }
    }

    private void shutdown() {
        if (!executor.isShutdown()) {
            log.info("Shutting down");
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(2000, TimeUnit.MILLISECONDS)) { //optional *
                    log.warn("Executor did not terminate in the specified time."); //optional *
                    List<Runnable> droppedTasks = executor.shutdownNow(); //optional **
                    log.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed."); //optional **
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void populateCustomers(){

        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);

        Session session = connector.getSession();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        KafkaGenericTemplate<AvroCustomer> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroCustomer> customerKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        customerKafkaTemplate.setDefaultTopic(Constants.CUSTOMERS_TOPIC);

        CustomerRepository customerRepository = new CustomerRepository(session);
        List<Customer> customers = customerRepository.selectAll();

        customers.forEach(customer -> {
            System.out.println("Writing customeer for '" + customer.getCustomerid() + "' to input topic " +
                    Constants.CUSTOMERS_TOPIC);
            customerKafkaTemplate.sendDefault(new AvroCustomer(customer.getCustomerid(), customer.getCity()));
        });

        connector.close();
    }



    public static void populateProducts() throws Exception {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        ProductRepository br = new ProductRepository(session);
        List<Product>  products = br.selectAll();
        List<AvroProduct> avroProducts = ProductRepository.mapAvroProducts(products);

        KafkaGenericTemplate<AvroProduct> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroProduct> productKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        productKafkaTemplate.setDefaultTopic(Constants.PRODUCTS_TOPIC);

        avroProducts.forEach(product -> {
            System.out.println("Writing inventory for '" + product.getProduct() + "' to input topic " + Constants.PRODUCTS_TOPIC);
            productKafkaTemplate.sendDefault(product);
        });

        connector.close();
    }

    public static void populateOrders() throws Exception {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("127.0.0.1", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.useKeyspace("ks1");

        OrderRepository br = new OrderRepository(session);

        List<Order>  orders = br.selectAll();
        List<AvroOrder> avroOrders = OrderRepository.mapAvroOrders(orders);

        KafkaGenericTemplate<AvroOrder> kafkaGenericTemplate = new KafkaGenericTemplate<>();
        KafkaTemplate<String, AvroOrder> ordersKafkaTemplate = kafkaGenericTemplate.getKafkaTemplate();
        ordersKafkaTemplate.setDefaultTopic(Constants.ORDERS_TOPIC);

        avroOrders.forEach(order -> {
            System.out.println("Writing Order for customer '" + order.getCustomerId() + "' to input topic " + Constants.ORDERS_TOPIC);
            ordersKafkaTemplate.sendDefault(order);
        });

        connector.close();
    }

}
