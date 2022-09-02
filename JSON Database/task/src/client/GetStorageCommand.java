package client;

public class GetStorageCommand implements ICommand {

    private final JsonAction jsonAction;
    private final String key;

    public GetStorageCommand(JsonAction jsonAction, String key) {
        this.jsonAction = jsonAction;
        this.key = key;
    }

    @Override
    public void execute() {
        jsonAction.setType("get");
        jsonAction.setKey(key);
    }
}
