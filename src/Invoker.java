package src;

public class Invoker {
    private Command command;
    void setCommand(Command command){
        this.command = command;
    }
    void performCommand(){
        this.command.execute();
    }
}
