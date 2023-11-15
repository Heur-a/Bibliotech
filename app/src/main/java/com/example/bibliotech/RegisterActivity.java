package com.example.bibliotech;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final short MIN_DIGITS = 8;

    private EditText password;
    private EditText repeatPassword;
    private EditText email;
    private EditText repeatEmail;
    private EditText name;
    private EditText surnames;

    private String textPassword;
    private String textEmail;
    private String textName;
    private String textSurnames;
    private User Credentials;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);

        // Initialize EditText variables
        email = findViewById(R.id.editTextMail);
        repeatEmail = findViewById(R.id.editTextConfirmRepeatEmail);
        password = findViewById(R.id.editTextPasswordSignIn);
        repeatPassword = findViewById(R.id.editTextConfirmPassword);
        name = findViewById(R.id.editTextName);
        surnames = findViewById(R.id.editTextSurnames);

        // Initialize String variables
        textPassword = password.getText().toString();
        textEmail = email.getText().toString();
        textName = name.getText().toString();
        textSurnames = surnames.getText().toString();
        Credentials = new User(textEmail, textPassword, textName, textSurnames);

        //Initialize buttons
        Button createAccountButton = findViewById(R.id.buttonCrearCuenta);
        ImageButton togglePassword = findViewById(R.id.imageButton2);
        ImageButton toggleRepeatPassword = findViewById(R.id.imageButton);


        //Add listeners to buttons
        createAccountButton.setOnClickListener(task -> {
            //Refresh String variables
            textPassword = password.getText().toString();
            textEmail = email.getText().toString();
            textName = name.getText().toString();
            textSurnames = surnames.getText().toString();
            Credentials = new User(textEmail, textPassword, textName, textSurnames);
            //Begin createUser process
            createUser();
        });

        //Buttons for toggling password and repeat password visibility
        togglePassword.setOnClickListener(task -> {
            if (password != null) {
                togglePasswordVisibility(password);
            }
        });

        toggleRepeatPassword.setOnClickListener(task -> {
            if(repeatPassword != null) {
                togglePasswordVisibility(repeatPassword);
            }
        });

    }

    // Methods for checking if the textPrompts are good
    private boolean match(String a, String b) {
        return Objects.equals(a, b);
    }

    private boolean minPasswordLength() {
        return textPassword.length() <= MIN_DIGITS;
    }

    private boolean validEmail() {
        return !textEmail.contains("@") || !textEmail.contains(".");
    }

    protected void createUser() {
        if (validateFields()) {
            FireBaseActions.getInstance();
            FireBaseActions.createUser(Credentials, this, this, MainActivity.class);
        }
    }

    static public void togglePasswordVisibility(@NonNull EditText passwordField) {
        Log.d("RegisterActivity", "togglePasswordVisibility: Function called");
        // Obtenir el tipus actual d'entrada del text (password o text normal)
        int inputType = passwordField.getInputType();

        // Comprovar si el tipus actual és de contrasenya
        boolean isPassword = (inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD) > 0;

        // Canviar el tipus d'entrada segons l'estat actual
        if (isPassword) {
            // Si és una contrasenya, canviar-lo a text normal (visible)
            passwordField.setInputType(inputType & ~InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // Si és text normal (visible), canviar-lo a contrasenya (ocult)
            passwordField.setInputType(inputType | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        // Mover el cursor al final del text per a garantir que siga visible
        passwordField.setSelection(passwordField.getText().length());
    }



    private boolean validateFields() {
        boolean isValid = true;

        if (!match(textPassword, repeatPassword.getText().toString())) {
            repeatPassword.setError("Passwords do not match");
            isValid = false;
        } else {
            repeatPassword.setError(null);
        }

        if (textName.isEmpty()) {
            name.setError("Name is empty");
            isValid = false;
        } else {
            name.setError(null);
        }

        if (textSurnames.isEmpty()) {
            surnames.setError("Surnames cannot be empty");
            isValid = false;
        } else {
            surnames.setError(null);
        }

        if (minPasswordLength()) {
            password.setError("Password must have at least " + MIN_DIGITS + " characters");
            isValid = false;
        } else {
            password.setError(null);
        }

        if (validEmail()) {
            email.setError("Invalid email address");
            isValid = false;
        } else {
            email.setError(null);
        }

        boolean hasDigit = false;
        boolean hasUppercase = false;

        for (char c : textPassword.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
        }

        if (!hasDigit) {
            password.setError("Password must contain at least one number");
            isValid = false;
        } else {
            isValid = true;
        }

        if (!hasUppercase) {
            password.setError("Password must contain at least one uppercase letter");
            isValid = false;
        } else {
            isValid = true;
        }

        if (!match(textEmail, repeatEmail.getText().toString())) {
            repeatEmail.setError("Emails must be the same");
            isValid = false;
        } else {
            repeatEmail.setError(null);
        }

        return isValid;
    }
}
