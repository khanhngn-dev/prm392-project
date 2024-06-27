package utils.https.types.request;

public class PreSignedRequest {
    private String fileName;

    public PreSignedRequest(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
