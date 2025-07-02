package com.summerpep.first.Starting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Engine engine(){
        return new Engine();
    }

    @Bean
    public Car car(Engine engine){
        return new Car(engine);     // DI happens here
    }
}