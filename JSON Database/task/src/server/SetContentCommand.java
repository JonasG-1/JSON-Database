package server;

public class SetContentCommand implements ICommand {

    private final int index;
    private final String content;
    private final Storage storage;
    private boolean success;

    public SetContentCommand(Storage storage, int index, String content) {
        this.storage = storage;
        this.index = index;
        this.content = content;
        success = false;
    }

    @Override
    public void execute() {
        this.success = storage.setContent(content, index);
    }

    @Override
    public String getStatus() {
        return success ? "OK" : "ERROR";
    }
}
