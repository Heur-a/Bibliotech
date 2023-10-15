package com.example.bibliotech;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class CustomLoginActivity extends AppCompatActivity {

    //Object for making all authoritazion objects
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    //strings wich contain mail and password
    private String mail, password = "";
    private ViewGroup container;
    private EditText editTextMail, editTextPassword;
    private TextInputLayout TextInMail, TextInPassword;
    private ProgressDialog dialog;
    private Button googleButton;

    private GoogleSignInClient googleSignInClient;
    private static final int RC_GOOGLE_SIGN_IN = 123;


    protected void onCreate(Bundle savedInstanceState) {
        verificaSiUsuarioValidado();
        super.onCreate(savedInstanceState);

        //Asign variables their respective values

        setContentView(R.layout.activity_custom_login);
        editTextMail = (EditText) findViewById(R.id.correo);
        editTextPassword = (EditText) findViewById(R.id.contraseña);
        TextInMail = (TextInputLayout) findViewById(R.id.til_correo);
        TextInPassword = (TextInputLayout) findViewById(R.id.til_contraseña);
        container = (ViewGroup) findViewById(R.id.contenedor);
        googleButton =  findViewById(R.id.buttonGoogle);
        configureGoogle();
        googleButton.setOnClickListener( View   -> {

                Intent i = googleSignInClient.getSignInIntent();
                startActivityForResult(i, RC_GOOGLE_SIGN_IN);
            }
        );
        dialog = new ProgressDialog(this);
        dialog.setTitle("Verificando usuario");
        dialog.setMessage("Por favor espere...");
    }
    @Override public void onActivityResult(int requestCode, int resultCode,
                                           Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleAuth(account.getIdToken());
            } catch (ApiException e) {
                mensaje("Error de autentificación con Google");
            }
        }
    }

    private void googleAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,
                null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            verificaSiUsuarioValidado();
                        }else{
                            mensaje(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    private void configureGoogle() {
        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("92021108070-7f6sfrjqsr553632c6iqh5mq59j29heb.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
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

    public void inicioSesiónCorreo(View v) {
        if (verificaCampos()) {
            dialog.show();
            auth.signInWithEmailAndPassword(mail, password)
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


    //TODO: Move this function to a separete SignIn activity :D
    public void registroCorreo(View v) {
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
        TextInMail.setError("");
        TextInPassword.setError("");

        try {
            if (mail.isEmpty()) {
                throw new ValidationException("Introduce un correo");
            }

            if (!mail.contains("@") || !mail.contains(".")) {
                throw new ValidationException("Correo no válido");
            }

            if (password.isEmpty()) {
                throw new ValidationException("Introduce una contraseña");
            }

            if (password.length() < 6) {
                throw new ValidationException("Ha de contener al menos 6 caracteres");
            }

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
                throw new ValidationException("Ha de contener un número");
            }

            if (!hasUppercase) {
                throw new ValidationException("Ha de contener una letra mayúscula");
            }

            return true;

        } catch (ValidationException e) {
            TextInMail.setError(e.getMessage());
            TextInPassword.setError(e.getMessage());
            return false;
        }
    }

    // Custom ValidationException class
    static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }



}

