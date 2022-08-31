package client;

public class GetStorageCommand implements ICommand {

    private final Client client;
    private final int index;

    public GetStorageCommand(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void execute() {
        client.sendMessage(String.format("get %d", index));
    }
}
