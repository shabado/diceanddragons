package shabadoit.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

//TODO revert this to !test when ready to deploy somewhere
@Profile("!test")
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
        String password = environment.getRequiredProperty("mongodb.password");
        MongoClientURI uri = new MongoClientURI("mongodb+srv://danddapp:"+password+"@diceanddragons-yrn1p.mongodb.net/dandd?retryWrites=true");
        return new MongoClient(uri);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
