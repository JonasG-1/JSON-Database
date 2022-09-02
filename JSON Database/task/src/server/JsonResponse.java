package server;

public class JsonResponse {
    String response;
    String reason;
    String value;

    public void setResponse(boolean response) {
        this.response = response ? ResponseType.OK.name() : ResponseType.ERROR.name();
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
