package server;

import com.google.gson.JsonElement;

public class SetContentCommand implements ICommand {

    private final JsonElement keyElement;
    private final JsonElement value;
    private final Storage storage;
    private boolean response;
    private final String reason;

    public SetContentCommand(Storage storage, JsonElement keyElement, JsonElement value) {
        this.storage = storage;
        this.keyElement = keyElement;
        this.value = value;
        response = false;
        reason = null;
    }

    @Override
    public void execute() {
        this.response = storage.setContent(keyElement, value);
    }

    @Override
    public JsonResponse getResponse() {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setResponse(response);
        jsonResponse.setReason(reason);
        return jsonResponse;
    }
}
