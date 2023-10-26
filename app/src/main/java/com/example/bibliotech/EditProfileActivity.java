package com.example.bibliotech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class EditProfileActivity extends AppCompatActivity {
    private TextView name, surnames, email, category;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfileditar); // Set the layout for this activity
        image = findViewById(R.id.imageViewProfile);
        name = findViewById(R.id.textViewNameProfile);
        surnames = findViewById(R.id.textViewSurnames);
        category = findViewById(R.id.textViewCategoryProfile);
        email = findViewById(R.id.textViewEmailProfile);
        Button B = findViewById(R.id.guardar);
        updateNames();
        B.setOnClickListener(task -> {
            changeCredentials();
        });

    }

    private void updateNames () {
        UserCredentials credentials = FireBaseActions.getCredentials(this);
        name.setText(credentials.username);
        email.setText(credentials.email);
        Glide.with(this)
                .load(credentials.photoUri)
                .into(image);
    }
    public void onClick(View view) {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(EditProfileActivity.this, perfilActivity.class);
        startActivity(intent);
    }


    private void changeCredentials() {
        if (verificaCampos()) {
            String nameString = name.getText().toString();
            String emailString = email.getText().toString();
            FireBaseActions.updateEmail(emailString, this);
            FireBaseActions.updateUsername(nameString);

            // Crea un Bundle per emmagatzemar les dades que vols actualitzar
            Bundle dadesActualitzades = new Bundle();
            dadesActualitzades.putString("nouNom", nameString);
            dadesActualitzades.putString("nouEmail", emailString);

            // Crea un intent per reiniciar l'activitat amb les dades actualitzades
            Intent intent = new Intent(EditProfileActivity.this, EditProfileActivity.class);
            intent.putExtras(dadesActualitzades);

            startActivity(intent);
            finish(); // Tanca l'activitat actual
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Obté les dades actualitzades del Bundle, si existeixen
        Bundle dadesActualitzades = getIntent().getExtras();
        if (dadesActualitzades != null) {
            String nouNom = dadesActualitzades.getString("nouNom");
            String nouEmail = dadesActualitzades.getString("nouEmail");

            // Actualitza la interfície gràfica amb les noves dades
            name.setText(nouNom);
            email.setText(nouEmail);
        }
    }

    private boolean verificaCampos() {
        String mail = email.getText().toString();
        email.setError(null);
        boolean isValid = true;

        if (mail.isEmpty()) {
            email.setError("Enter an email");
            isValid = false;
        } else if (!mail.contains("@") || !mail.contains(".")) {
            email.setError("Invalid email format");
            isValid = false;
        }
        return isValid;
    }
}
