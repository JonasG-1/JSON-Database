package client;

public class SetStorageCommand implements ICommand {

    private final Client client;
    private final int index;
    private final String content;

    public SetStorageCommand(Client client, int index, String content) {
        this.client = client;
        this.index = index;
        this.content = content;
    }

    @Override
    public void execute() {
        client.sendMessage(String.format("set %d %s", index, content));
    }
}
