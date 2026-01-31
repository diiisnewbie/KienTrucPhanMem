package iuh.fit.statePattern;

public class NewOrderState implements OrderState{

    @Override
    public void next(OrderContext context) {
        context.setState(new PaidOrderState());
    }

    @Override
    public void prev(OrderContext context) {
        System.out.println("The order is in its initial state.");
    }

    @Override
    public void printStatus() {
        System.out.println("Order is new.");
    }
}
