package server;

public class DeleteContentCommand implements ICommand {

    private final int index;
    private final Storage storage;
    private boolean success;

    public DeleteContentCommand(Storage storage, int index) {
        this.storage = storage;
        this.index = index;
        this.success = false;
    }

    @Override
    public void execute() {
        success = storage.deleteContent(index);
    }

    @Override
    public String getStatus() {
        return success ? "OK" : "ERROR";
    }
}
