package iuh.fit.statePattern;

public class PaidOrderState implements OrderState{

    @Override
    public void next(OrderContext context) {
        context.setState(new ShippedOrderState());
    }

    @Override
    public void prev(OrderContext context) {
        context.setState(new NewOrderState());
    }

    @Override
    public void printStatus() {
        System.out.println("Order is paid.");
    }
}
