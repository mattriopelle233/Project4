package src;

public class GetNewSalesperson implements Command {
    Receiver receiver;

    public GetNewSalesperson(Receiver receiver) {
        this.receiver = receiver;
    }

    public void execute() {
        receiver.getNewSalesperson();
    }
}
