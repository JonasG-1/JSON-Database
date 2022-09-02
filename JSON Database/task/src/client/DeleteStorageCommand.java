package client;

public class DeleteStorageCommand implements ICommand {

    private final JsonAction jsonAction;
    private final String key;

    public DeleteStorageCommand(JsonAction jsonAction, String key) {
        this.jsonAction = jsonAction;
        this.key = key;
    }

    @Override
    public void execute() {
        jsonAction.setType("delete");
        jsonAction.setKey(key);
    }
}
