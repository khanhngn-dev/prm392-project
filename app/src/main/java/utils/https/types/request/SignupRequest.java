package utils.https.types.request;

public class SignupRequest {
    private String id;
    private String email;

    public SignupRequest(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public SignupRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
