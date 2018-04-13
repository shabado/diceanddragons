package shabadoit.com.config;

import org.springframework.context.annotation.Bean;
import shabadoit.com.controller.SpellController;
import shabadoit.com.service.SpellService;
import shabadoit.com.service.impl.SpellServiceImpl;

//This class is required due to @DataMongoDb behaviour, do not annotate with @Configuration
public class TestConfig {

    @Bean
    public SpellController spellController() {
        return new SpellController();
    }

    @Bean
    public SpellService spellService() {
        return new SpellServiceImpl();
    }
}
