package client;

public class ExitStorageCommand implements ICommand {

    private final Client client;

    public ExitStorageCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        client.sendMessage("exit");
    }
}
