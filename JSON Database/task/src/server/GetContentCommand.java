package server;

public class GetContentCommand implements Command {

    private final int index;
    private final Storage storage;
    private String content;

    public GetContentCommand(Storage storage, int index) {
        this.storage = storage;
        this.index = index;
        this.content = "ERROR";
    }

    @Override
    public void execute() {
        this.content = storage.getContent(index);
    }

    @Override
    public String getStatus() {
        return this.content == null ? "ERROR" : this.content;
    }
}
