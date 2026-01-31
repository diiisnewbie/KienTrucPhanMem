package iuh.fit.stategyPattern;

public class ShippingContext {

    private ShippingStrategy strategy;

    public void setStrategy(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double distance) {
        return strategy.calculateFee(distance);
    }
}
