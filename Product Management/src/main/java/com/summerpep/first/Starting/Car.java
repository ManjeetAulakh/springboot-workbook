package com.summerpep.first.Starting;

public class Car {
    private final Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }

    public void drive(){
        System.out.println("Car is staring and calling engine");
        engine.start();
    }
}
