package iuh.fit.stategyPattern;

public class FastShipping implements ShippingStrategy {
    @Override
    public double calculateFee(double distance) {
        return distance * 2;
    }
}
