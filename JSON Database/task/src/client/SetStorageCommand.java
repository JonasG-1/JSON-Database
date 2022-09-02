package client;

public class SetStorageCommand implements ICommand {

    private final JsonAction jsonAction;
    private final String key;
    private final String value;

    public SetStorageCommand(JsonAction jsonAction, String key, String value) {
        this.jsonAction = jsonAction;
        this.key = key;
        this.value = value;
    }

    @Override
    public void execute() {
        jsonAction.setType("set");
        jsonAction.setKey(key);
        jsonAction.setValue(value);
    }
}
