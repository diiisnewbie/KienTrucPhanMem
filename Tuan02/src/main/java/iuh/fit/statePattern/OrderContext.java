package iuh.fit.statePattern;

public class OrderContext {
    private OrderState state;

    public OrderContext() {
        state = new NewOrderState();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void nextState() {
        state.next(this);
    }

    public void prevState() {
        state.prev(this);
    }

    public void printStatus() {
        state.printStatus();
    }
}
