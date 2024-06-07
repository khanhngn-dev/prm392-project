package utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static void signOut() {
        auth.signOut();
    }
    public static FirebaseUser currentUser() {
        return auth.getCurrentUser();
    }
}
