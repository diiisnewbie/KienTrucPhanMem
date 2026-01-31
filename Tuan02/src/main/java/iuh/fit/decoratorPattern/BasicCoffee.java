package iuh.fit.decoratorPattern;

public class BasicCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Basic Coffee";
    }

    @Override
    public double cost() {
        return 20000;
    }
}
