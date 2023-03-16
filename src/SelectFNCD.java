package src;

public class SelectFNCD implements Command {
    Receiver receiver;

    public SelectFNCD(Receiver receiver) {
        this.receiver = receiver;
    }

    public void execute() {
        receiver.selectFNCD();
    }
}
