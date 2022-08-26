package client;

public class DeleteStorageCommand implements Command {

    private final Client client;
    private final int index;

    public DeleteStorageCommand(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void execute() {
        client.sendMessage(String.format("delete %d", index));
    }
}
