package server;

public class SetContentCommand implements ICommand {

    private final String key;
    private final String value;
    private final Storage storage;
    private boolean success;
    private final String out;

    public SetContentCommand(Storage storage, String key, String value) {
        this.storage = storage;
        this.key = key;
        this.value = value;
        success = false;
        out = null;
    }

    @Override
    public void execute() {
        this.success = storage.setContent(key, value);
    }

    @Override
    public boolean getResponse() {
        return success;
    }

    @Override
    public String getOutput() {
        return out;
    }
}
