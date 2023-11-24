package com.example.bibliotech;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class CustomLoginActivity extends AppCompatActivity {

    private static final int RC_GOOGLE_SIGN_IN = 123;

    private Book user;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private ProgressDialog dialog;

    private EditText editTextMail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        configureGoogle();

        editTextMail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button googleButton = findViewById(R.id.buttonGooglee);
        Button logIn = findViewById(R.id.buttonLogIn);
        ImageButton togglePassword = findViewById(R.id.imageButton3);

        // Set click listeners for buttons
        googleButton.setOnClickListener(this::signInWithGoogle);
        logIn.setOnClickListener(this::inicioSesionCorreo);
        togglePassword.setOnClickListener(task -> RegisterActivity.togglePasswordVisibility(editTextPassword));

        // Initialize ProgressDialog
        dialog = new ProgressDialog(this);
        dialog.setTitle("Verifying User");
        dialog.setMessage("Please wait...");
    }

    private void configureGoogle() {
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("92021108070-o38qkvb463362i6hpuimna25slbq795d.apps.googleusercontent.com")  // Replace with your web client ID
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String displayName = firebaseUser.getDisplayName();
                                Log.d(TAG, "Firebase User Name: " + displayName);
                            }
                            verificaSiUsuarioValidado(MainActivity.class);
                        }
                    });
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            verificaSiUsuarioValidado(MainActivity.class);
        }
    }

    public void verificaSiUsuarioValidado(Class Class) {
        if (auth.getCurrentUser() != null) {
            // User is signed in, navigate to the main activity
            Intent i = new Intent(this, Class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void inicioSesionCorreo(View view) {
        if (verificaCampos()) {
            dialog.show();
            auth.signInWithEmailAndPassword(editTextMail.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            verificaSiUsuarioValidado(MainActivity.class);
                        } else {
                            dialog.dismiss();
                            mensaje("The provided email does not exist");
                        }
                    });
        }
    }

    private void signInWithGoogle(View view) {
        // Start Google Sign-In process
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void mensaje(String mensaje) {
        // Show a Snackbar message
        Snackbar.make(findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_LONG).show();
    }

    private boolean verificaCampos() {
        String mail = editTextMail.getText().toString();
        String password = editTextPassword.getText().toString();
        editTextMail.setError(null);
        editTextPassword.setError(null);
        boolean isValid = true;

        if (mail.isEmpty()) {
            editTextMail.setError("Enter an email");
            isValid = false;
        } else if (!mail.contains("@") || !mail.contains(".")) {
            editTextMail.setError("Invalid email format");
            isValid = false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Enter a password");
            isValid = false;
        } else if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            boolean hasDigit = false;
            boolean hasUppercase = false;
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c)) {
                    hasDigit = true;
                }
                if (Character.isUpperCase(c)) {
                    hasUppercase = true;
                }
            }
            if (!hasDigit) {
                editTextPassword.setError("Password must contain at least one number");
                isValid = false;
            }
            if (!hasUppercase) {
                editTextPassword.setError("Password must contain at least one uppercase letter");
                isValid = false;
            }
        }
        return isValid;
    }
}
