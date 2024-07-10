package utils.https;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import utils.https.types.request.SignupRequest;
import utils.https.types.response.SignupResponse;

public interface ProjectAPI {
    //Auths
    @POST("/auths/signup")
    Call<ApiResponse<SignupResponse>> signup(@Body SignupRequest request);
}
