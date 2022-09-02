package server;

public class GetContentCommand implements ICommand {

    private final String key;
    private final Storage storage;
    private String content;
    private boolean success;

    public GetContentCommand(Storage storage, String key) {
        this.storage = storage;
        this.key = key;
        this.content = "ERROR";
        this.success = false;
    }

    @Override
    public void execute() {
        this.content = storage.getContent(key);
        if (content != null) {
            success = true;
        } else {
            content = "No such key";
        }
    }

    @Override
    public boolean getResponse() {
        return success;
    }

    @Override
    public String getOutput() {
        return content;
    }
}
