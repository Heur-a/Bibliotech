package com.example.bibliotech;

import android.os.Bundle;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNames();
    }

    private void updateNames () {
        UserCredentials credentials = FireBaseActions.getCredentials(this);
        name.setText(credentials.username);
        email.setText(credentials.email);
        Glide.with(this)
                .load(credentials.photoUri)
                .into(image);
    }
}
