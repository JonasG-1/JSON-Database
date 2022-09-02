package client;

public class ExitStorageCommand implements ICommand {

    private final JsonAction jsonAction;

    public ExitStorageCommand(JsonAction jsonAction) {
        this.jsonAction = jsonAction;
    }

    @Override
    public void execute() {
        jsonAction.setType("exit");
    }
}
