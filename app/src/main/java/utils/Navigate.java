package utils;

import android.content.Context;
import android.content.Intent;

public class Navigate {
    public static void navigate(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
}
