package server;

import com.google.gson.JsonElement;

public class GetContentCommand implements ICommand {

    private final JsonElement keyElement;
    private final Storage storage;
    private JsonElement value;
    private String reason;
    private boolean response;

    public GetContentCommand(Storage storage, JsonElement keyElement) {
        this.storage = storage;
        this.keyElement = keyElement;
        this.reason = null;
        this.response = false;
    }

    @Override
    public void execute() {
        this.value = storage.getContent(keyElement);
        if (value != null) {
            response = true;
        } else {
            reason = "No such key";
        }
    }

    @Override
    public JsonResponse getResponse() {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setResponse(response);
        jsonResponse.setReason(reason);
        jsonResponse.setValue(value);
        return jsonResponse;
    }
}
