package utils.https;


import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface NonNullCallback<T> extends Callback<T> {
    void onResponse(@NonNull Call<T> call, @NonNull Response<T> response);

    void onFailure(@NonNull Call<T> call, @NonNull Throwable t);
}