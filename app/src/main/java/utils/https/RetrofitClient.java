package utils.https;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.function.Function;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Auth;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://prm392.tripllery.com/";

    public static void getAPIClient(Function<ProjectAPI, Void> callback) {
        getClient(retrofit -> {
            ProjectAPI api = retrofit.create(ProjectAPI.class);
            callback.apply(api);

            return null;
        });
    }

    private static void getClient(Function<Retrofit, Void> callback) {
        FirebaseUser user = Auth.currentUser();
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String idToken = task.getResult().getToken();

                Retrofit retrofit = getClient(idToken);
                callback.apply(retrofit);
            } else {
                Log.e("FirebaseAuth", "Failed to get user id token");
            }
        });
    }

    private static Retrofit getClient(String idToken) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", "Bearer " + idToken)
                                    .method(original.method(), original.body());
                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
