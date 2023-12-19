package com.example.bibliotech.presentacion;


import static com.example.bibliotech.presentacion.perfilActivity.getNames;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.datos.firestore.UserFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private TextView name, surnames, email, category;
    private ImageView image;

    private StorageReference ref;
    private UserFirestore db;

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

    private void updateNames() {
        db = new UserFirestore();
        getNames(db, new UserFirestore.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                name.setText(user.getName());
                email.setText(user.getEmail());
                surnames.setText(user.getSurnames());
                // Load image from Firebase Storage using FirebaseImageLoader
                FirebaseStorage.getInstance()
                        .getReference().child("images/pfp/" + FireBaseActions.getUserId()).getDownloadUrl().addOnSuccessListener(task -> {
                            Glide.with(getApplicationContext())
                                    .load(task)
                                    .into(image);
                        });
            }

            @Override
            public void onUserError(Exception e) {
                Log.d("Firebase GET", "Error loading user: " + e.getMessage());
            }
        });
    }


    public void onClick(View view) {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(EditProfileActivity.this, perfilActivity.class);
        startActivity(intent);
    }


    private void changeCredentials() {
        if (verificaCampos()) {

            // Actualiza en authentication
            String nameString = name.getText().toString();
            String emailString = email.getText().toString();
            String surnamesString = surnames.getText().toString();
            FireBaseActions.updateEmail(emailString, this);
            FireBaseActions.updateUsername(nameString);


            //Actualiza en BBDD

            User update = new User();
            update.setId(FireBaseActions.getUserId());
            update.setName(nameString);
            update.setEmail(emailString);
            update.setSurnames(surnamesString);

            UserFirestore userFirestore = new UserFirestore();

            userFirestore.add(update);

            // Crea un intent per reiniciar l'activitat amb les dades actualitzades
            Intent intent = new Intent(EditProfileActivity.this, perfilActivity.class);
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

    //Get pfp form the database
    //Each pfp will be named after the uid of its user
    private void getProfilePicture(String id) {
        ref = FirebaseStorage.getInstance().getReference();
        StorageReference imgPool = ref.child(getResources()
                .getString(R.string.pfp_image_path));


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
