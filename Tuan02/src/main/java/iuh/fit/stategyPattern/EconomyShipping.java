package iuh.fit.stategyPattern;

public class EconomyShipping implements ShippingStrategy {
    @Override
    public double calculateFee(double distance) {
        return distance * 1;
    }
}
