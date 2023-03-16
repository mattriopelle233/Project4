package src;

public class GetItemDetails implements Command {
    Receiver receiver;

    public GetItemDetails(Receiver receiver) {
        this.receiver = receiver;
    }

    public void execute() {
        receiver.getItemDetails();
    }
}
