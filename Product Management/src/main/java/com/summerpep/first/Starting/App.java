package com.summerpep.first.Starting;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Car car = context.getBean("myCar", Car.class);
        car.drive();

        ApplicationContext context1 = new AnnotationConfigApplicationContext(AppConfig.class);
        Car car1 = context1.getBean("car", Car.class);
        car1.drive();
    }
}
