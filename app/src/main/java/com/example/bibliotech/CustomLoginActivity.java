package com.example.bibliotech;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.Objects;

public class CustomLoginActivity extends AppCompatActivity {

    //Object for making all authoritazion objects
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    //strings wich contain mail and password
    private String mail, password = "";
    private ViewGroup container;
    private EditText editTextMail, editTextPassword;
    private ProgressDialog dialog;

    private GoogleSignInClient googleSignInClient;

    private static final int RC_GOOGLE_SIGN_IN = 123;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verificaSiUsuarioValidado();

        // Configura Google Sign-In
        configureGoogle();



        setContentView(R.layout.inicio_sesion);
        editTextMail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        container = findViewById(R.id.contenedor);
        Button googleButton = findViewById(R.id.buttonGooglee);
        Button logIn = findViewById(R.id.buttonLogIn);
        googleButton.setOnClickListener(action -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
        });

        logIn.setOnClickListener(action -> inicioSesiónCorreo());
        dialog = new ProgressDialog(this);
        dialog.setTitle("Verificando usuario");
        dialog.setMessage("Por favor espere...");
    }



    protected void configureGoogle() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("92021108070-o38qkvb463362i6hpuimna25slbq795d.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // L'usuari s'ha autenticat amb èxit
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Imprimeix el nom de l'usuari
                                String displayName = firebaseUser.getDisplayName();
                                Log.d(TAG, "Nom de l'usuari de Firebase: " + displayName);
                            }
                            verificaSiUsuarioValidado();
                        }
                    });
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            verificaSiUsuarioValidado();
        }
    }



    private void verificaSiUsuarioValidado() {
        //IF signed in continue, if not return
        if (auth.getCurrentUser() != null) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    public void inicioSesiónCorreo() {
        if (verificaCampos()) {
            dialog.show();
            auth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            verificaSiUsuarioValidado();
                        } else {
                            dialog.dismiss();
                            mensaje("El correo implementado no existe");
                        }
                    });
        }
    }


    //TODO: Move this function to a separete SignIn activity :D
    public void registroCorreo() {
        if (verificaCampos()) {
            dialog.show();
            auth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            verificaSiUsuarioValidado();
                        } else {
                            dialog.dismiss();
                            mensaje(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                        }
                    });
        }
    }

    private void mensaje(String mensaje) {
        Snackbar.make(container, mensaje, Snackbar.LENGTH_LONG).show();
    }

    //Verfiy each input is in an operable state
    private boolean verificaCampos() {
        mail = editTextMail.getText().toString();
        password = editTextPassword.getText().toString();
        editTextMail.setError(null);
        editTextPassword.setError(null);

        boolean isValid = true;

        if (mail.isEmpty()) {
            editTextMail.setError("Introduce un correo");
            isValid = false;
        } else if (!mail.contains("@") || !mail.contains(".")) {
            editTextMail.setError("Correo no válido");
            isValid = false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Introduce una contraseña");
            isValid = false;
        } else if (password.length() < 6) {
            editTextPassword.setError("La contraseña debe tener al menos 6 caracteres");
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
                editTextPassword.setError("La contraseña debe contener al menos un número");
                isValid = false;
            }

            if (!hasUppercase) {
                editTextPassword.setError("La contraseña debe contener al menos una letra mayúscula");
                isValid = false;
            }
        }

        return isValid;
    }


}

