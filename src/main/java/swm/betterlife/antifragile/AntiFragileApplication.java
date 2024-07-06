package swm.betterlife.antifragile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories // MongoRepository 사용하기 위함
public class AntiFragileApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntiFragileApplication.class, args);
    }

}