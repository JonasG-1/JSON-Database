package server;

public class CommandService {

    public Command convert(String command) {
        try {
            return Command.valueOf(command.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
