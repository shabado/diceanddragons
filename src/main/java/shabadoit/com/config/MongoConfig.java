package shabadoit.com.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

//TODO revert this to !test when ready to deploy somewhere
@Profile("dev")
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Autowired
    private Environment environment;

    @Override
    protected String getDatabaseName() {
        return environment.getRequiredProperty("mongodb.name");
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(environment.getRequiredProperty("mongodb.host"), Integer.parseInt(environment.getRequiredProperty("mongodb.port")));
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
