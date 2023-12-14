package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class reservasalaactivity extends AppCompatActivity {
    Button btn_qr;
    ImageView img_qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paginasreservas); // Set the layout for this activity
        btn_qr = findViewById(R.id.btn_qr);
        img_qr = findViewById(R.id.img_qr);
        
        btn_qr.setOnClickListener(v->{
            generateQR();
        });
    }

    private void generateQR() {
        String userID = FireBaseActions.getUserId();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(userID, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            img_qr.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        // Open the EditProfileActivity when the "editar" ImageView is clicked
        Intent intent = new Intent(reservasalaactivity.this, MainActivity.class);
        startActivity(intent);
    }

}
