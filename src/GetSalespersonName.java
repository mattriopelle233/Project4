package src;

public class GetSalespersonName implements Command {
    Receiver receiver;

    public GetSalespersonName(Receiver receiver) {
        this.receiver = receiver;
    }

    public void execute() {
        receiver.getSalespersonName();
    }
}
