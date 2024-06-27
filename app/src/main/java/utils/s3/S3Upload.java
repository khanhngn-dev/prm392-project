package utils.s3;

import android.content.Context;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class S3Upload {
    private static OkHttpClient client = null;

    public static void uploadFile(Context context, String presignedUrl, File file, Callback callback) {
        try {
            // Check file size
            if (file.length() > 5 * 1024 * 1024) {
                Toast.makeText(context, "File size exceeds 5MB", Toast.LENGTH_SHORT).show();
                throw new IOException("File size exceeds 5MB");
            }

            // Check file type
            String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());

            if (mimeType == null || !mimeType.startsWith("image/")) {
                Toast.makeText(context, "Please select an image file", Toast.LENGTH_SHORT).show();
                return;
            }

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody requestBody = RequestBody.create(file, mediaType);

            Request request = new Request.Builder()
                    .url(presignedUrl)
                    .put(requestBody)
                    .build();

            if (client == null) {
                client = new OkHttpClient();
            }

            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            Log.e("S3Upload", "Upload failed", e);
            Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show();
        }
    }
}
