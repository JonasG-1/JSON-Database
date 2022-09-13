package server;

import com.google.gson.JsonElement;

public class DeleteContentCommand implements ICommand {

    private final JsonElement keyElement;
    private final Storage storage;
    private boolean response;
    private String reason;

    public DeleteContentCommand(Storage storage, JsonElement keyElement) {
        this.storage = storage;
        this.keyElement = keyElement;
        this.response = false;
    }

    @Override
    public void execute() {
        response = storage.deleteContent(keyElement);
        if (!response) {
            reason = "No such key";
        }
    }

    @Override
    public JsonResponse getResponse() {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setResponse(response);
        jsonResponse.setReason(reason);
        return jsonResponse;
    }
}
