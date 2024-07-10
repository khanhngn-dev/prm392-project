package utils.https.types.response;

public class SignupResponse {
    private String id;
    private String role;

    public SignupResponse(String id, String role) {
        this.id = id;
        this.role = role;
    }

    public SignupResponse() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
