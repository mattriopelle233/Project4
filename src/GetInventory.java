package src;

public class GetInventory implements Command {
    private Receiver receiver;

    public GetInventory(Receiver receiver) {
        this.receiver = receiver;
    }

    public void execute() {
        receiver.getInventory();
    }
}
