package server;

public interface ICommand {

    void execute();

    boolean getResponse();

    String getOutput();
}
