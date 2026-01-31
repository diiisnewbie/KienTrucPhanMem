package iuh.fit;

import iuh.fit.decoratorPattern.BasicCoffee;
import iuh.fit.decoratorPattern.Coffee;
import iuh.fit.decoratorPattern.MilkDecorator;
import iuh.fit.decoratorPattern.SugarDecorator;
import iuh.fit.factoryPattern.Animal;
import iuh.fit.factoryPattern.Cat;
import iuh.fit.factoryPattern.Dog;
import iuh.fit.singletonPattern.EagerInitialization;
import iuh.fit.statePattern.OrderContext;
import iuh.fit.stategyPattern.EconomyShipping;
import iuh.fit.stategyPattern.FastShipping;
import iuh.fit.stategyPattern.ShippingContext;

public class Main {
    public static void main(String[] args) {
        Coffee coffee = new BasicCoffee();

        coffee = new MilkDecorator(coffee);
        coffee = new SugarDecorator(coffee);

        System.out.println(coffee.getDescription());
        System.out.println(coffee.cost());





    }
}