package iuh.fit.statePattern;

public class ShippedOrderState implements OrderState{

    @Override
    public void next(OrderContext context) {
        System.out.println("Order already shipped. No next state.");
    }

    @Override
    public void prev(OrderContext context) {
        context.setState(new PaidOrderState());
    }

    @Override
    public void printStatus() {
        System.out.println("Order is shipped.");
    }
}
