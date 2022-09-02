package server;

public class DeleteContentCommand implements ICommand {

    private final String key;
    private final Storage storage;
    private boolean success;
    private String out;

    public DeleteContentCommand(Storage storage, String key) {
        this.storage = storage;
        this.key = key;
        this.success = false;
    }

    @Override
    public void execute() {
        success = storage.deleteContent(key);
        if (!success) {
            out = "No such key";
        }
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
