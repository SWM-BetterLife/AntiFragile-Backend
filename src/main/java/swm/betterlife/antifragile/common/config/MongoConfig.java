package swm.betterlife.antifragile.common.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import swm.betterlife.antifragile.common.converter.ReadingLocalDateTimeToKstConverter;
import swm.betterlife.antifragile.common.converter.ReadingLocalDateToKstConverter;
import swm.betterlife.antifragile.common.converter.WritingLocalDateTimeToKstConverter;
import swm.betterlife.antifragile.common.converter.WritingLocalDateToKstConverter;

@Configuration
@EnableTransactionManagement
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(uri);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(
            new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName())
        );
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public MongoCustomConversions customConversions(
        WritingLocalDateTimeToKstConverter writingLocalDateTimeToKstConverter,
        ReadingLocalDateTimeToKstConverter readingLocalDateTimeToKstConverter,
        WritingLocalDateToKstConverter writingLocalDateToKstConverter,
        ReadingLocalDateToKstConverter readingLocalDateToKstConverter
    ) {
        return new MongoCustomConversions(
            Arrays.asList(
                writingLocalDateTimeToKstConverter, readingLocalDateTimeToKstConverter,
                writingLocalDateToKstConverter, readingLocalDateToKstConverter
            )
        );
    }

}