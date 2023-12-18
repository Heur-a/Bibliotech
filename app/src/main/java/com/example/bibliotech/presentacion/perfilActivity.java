 package com.example.bibliotech.presentacion;
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
import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;
import com.example.bibliotech.datos.firestore.UserFirestore;

import java.util.HashMap;
import java.util.Map;

 public class perfilActivity extends AppCompatActivity {

    TextView name, surnames, email, category;
    ImageView image;
     public static UserFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil); // Set the layout for this activity
        db = new UserFirestore();

        ImageView editarImageView = findViewById(R.id.editar);
        editarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the EditProfileActivity when the "editar" ImageView is clicked
                Intent intent = new Intent(perfilActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Button notificaciones = findViewById(R.id.btn_prf_notis);
       notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the EditProfileActivity when the "notificaciones" ImageView is clicked
                Intent intent = new Intent(perfilActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Button privacidad = findViewById(R.id.btn_prf_priv);
        privacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the EditProfileActivity when the "privacidad" ImageView is clicked
                Intent intent = new Intent(perfilActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Button idioma = findViewById(R.id.btn_prf_idioma);
        privacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the EditProfileActivity when the "idioma" ImageView is clicked
                Intent intent = new Intent(perfilActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });





        name =  findViewById(R.id.textViewNameProfile);
        surnames = findViewById(R.id.textViewSurnames);
        email =  findViewById(R.id.textViewEmailProfile);
        category =  findViewById(R.id.textViewCategoryProfile);
        image = findViewById(R.id.imageViewProfile);

        updateNames();



    }

     @Override
     protected void onResume() {
         super.onResume();
         updateNames();
     }

     private void updateNames() {
         db = new UserFirestore();
         getNames(db, new UserFirestore.UserCallback() {
             @Override
             public void onUserLoaded(User user) {
                 name.setText(user.getName());
                 email.setText(user.getEmail());
                 surnames.setText(user.getSurnames());
                 Glide.with(perfilActivity.this)
                         .load(user.getPhotoUri())
                         .into(image);
             }

             @Override
             public void onUserError(Exception e) {
                 Log.d("Firebase GET", "Error loading user: " + e.getMessage());
             }
         });
     }

     public static void getNames(UserFirestore db, UserFirestore.UserCallback callback) {
         String uid = FireBaseActions.getUserId();
         db.getUser(uid, callback);
     }
    public void atras(View view) {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(perfilActivity.this, MainActivity.class);
        startActivity(intent);
    }

}