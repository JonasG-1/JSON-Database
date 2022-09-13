package server;

public interface ICommand {

    void execute();

    JsonResponse getResponse();
}
