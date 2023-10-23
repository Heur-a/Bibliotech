package com.example.bibliotech;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.Contract;

public class FireBaseActions {

    static public FirebaseAuth auth;
    static public FirebaseUser user;

    
    public FireBaseActions() {
        // Inicialitza l'instància d'autenticació de Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @NonNull
    @Contract(" -> new")
    public static FireBaseActions getInstance() {
        return new FireBaseActions();
    }

    public static FirebaseUser getCurrentUser() {
        return user = auth.getCurrentUser();
    }

    public static void signOut() {
        auth.signOut();
    }

    public static <TResult> Task signInWithCredential(AuthCredential credential) {
        return auth.signInWithCredential(credential);
    }

    public  static void configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("92021108070-o38qkvb463362i6hpuimna25slbq795d.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
       // GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public  static void createUser(@NonNull UserCredentials UserCredentials, Context Context, Activity Activity, Class Class) {

        auth.createUserWithEmailAndPassword(UserCredentials.email, UserCredentials.password)
                .addOnCompleteListener(Activity, (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(UserCredentials.completeName)
                                .build();
                        //Update Profile with displayName
                        assert user != null;
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                        verificaSiUsuarioValidado(Context,Class,Activity);
                                    }
                                });

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }

    public static void verificaSiUsuarioValidado(Context Context, Class Class, Activity Activity) {
        if (auth.getCurrentUser() != null) {
            // User is signed in, navigate to the main activity
            Intent i = new Intent(Context, Class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(Context,i,null);
            Activity.finish();
        }
    }

    public static UserCredentials getCredentials (Context Context) {
        if (getCurrentUser() != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String id = user.getUid();


            return new UserCredentials(name, email,photoUrl,id);

        }
        Toast.makeText(Context,"La toma de datos ha fallado",Toast.LENGTH_SHORT);
        return null;
    }
}

