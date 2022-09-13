package server;

public class Controller {

    private ICommand command;

    public void setCommand(ICommand command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }

    public JsonResponse getResponse() {
        return command.getResponse();
    }
}
