package client;

public class ExitStorageCommand implements Command {

    private final Client client;

    public ExitStorageCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        client.sendMessage("exit");
    }
}
