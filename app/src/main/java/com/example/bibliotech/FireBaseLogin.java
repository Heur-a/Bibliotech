package com.example.bibliotech;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseLogin {

    private FirebaseAuth auth;

    
    public FireBaseLogin() {
        // Inicialitza l'instància d'autenticació de Firebase
        auth = FirebaseAuth.getInstance();
    }

    public static FireBaseLogin getInstance() {
        return new FireBaseLogin();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void signOut() {
        auth.signOut();
    }

    public <TResult> Task signInWithCredential(AuthCredential credential) {
        return auth.signInWithCredential(credential);
    }

    public  void configureGoogle() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("92021108070-o38qkvb463362i6hpuimna25slbq795d.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
       // GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

    }
}

