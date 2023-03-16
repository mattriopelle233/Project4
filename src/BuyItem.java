package src;

public class BuyItem implements Command {
    private Receiver receiver;

    public BuyItem(Receiver receiver) {
        this.receiver = receiver;
    }

    public void execute() {
        receiver.buyItem();
    }
}
