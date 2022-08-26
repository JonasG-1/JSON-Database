package server;

public class Controller {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }

    public String getStatus() {
        return command.getStatus();
    }
}
