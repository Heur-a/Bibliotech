 package com.example.bibliotech;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

 public class perfilActivity extends AppCompatActivity {

    TextView name, surnames, email, category;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil); // Set the layout for this activity

        ImageView editarImageView = findViewById(R.id.editar);
        editarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the EditProfileActivity when the "editar" ImageView is clicked
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

     private void updateNames () {
        User credentials = FireBaseActions.getCredentials(this);
        name.setText(credentials.username);
        email.setText(credentials.email);
         Glide.with(this)
                 .load(credentials.photoUri)
                 .into(image);
    }
    public void atras(View view) {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(perfilActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
