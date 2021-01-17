package org.mddarr.reactive.cassandra.config;



import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "org.mddarr.ride.query.service.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.username}")
    private String username;

    @Value("${cassandra.password}")
    private String password;

    @Value("${cassandra.contactpoints}")
    private String contactPoints;

    @Value("${cassandra.port}")
    private int port;

    @Value("${cassandra.keyspace}")
    private String keySpace;

    @Value("${cassandra.basepackages}")
    private String basePackages;

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {basePackages};
    }

    @Override
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        CassandraMappingContext context = new CassandraMappingContext();
        context.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), keySpace));
        return context;
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setUsername(username);
        cluster.setPassword(password);
        cluster.setContactPoints(contactPoints);
        cluster.setPort(port);
        cluster.setJmxReportingEnabled(false);
        return cluster;
    }


    public static Session createSession(String ip, int port){
        Cluster cluster;

        cluster = Cluster.builder()
                .addContactPoint(ip)
                .withPort(port)
                .build();

        Session session = cluster.connect();

        session.execute("CREATE KEYSPACE  ks1 WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }");
//        session.execute("DROP TABLE IF EXISTS cassandrait.counter");
//        session.execute("CREATE TABLE cassandrait.counter (key text, value counter, PRIMARY key(key));");
        System.out.println("THER FGA");
        return session;
    }




}