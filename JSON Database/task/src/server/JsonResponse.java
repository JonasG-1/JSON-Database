package server;

import com.google.gson.JsonElement;

public class JsonResponse {
    String response;
    String reason;
    JsonElement value;

    public void setResponse(boolean response) {
        this.response = response ? ResponseType.OK.name() : ResponseType.ERROR.name();
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }
}
