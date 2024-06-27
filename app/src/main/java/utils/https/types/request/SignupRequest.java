package utils.https.types.request;

public class SignupRequest {
    private String id;

    public SignupRequest(String id) {
        this.id = id;
    }

    public SignupRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
