package src;

public class GetTime implements Command {
    Receiver receiver;

    public GetTime(Receiver receiver) {
        this.receiver = receiver;
    }

    public void execute() {
        receiver.getTime();
    }
}
