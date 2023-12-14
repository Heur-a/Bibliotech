package com.example.bibliotech.presentacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotech.datos.firestore.FireBaseActions;
import com.example.bibliotech.MainActivity;
import com.example.bibliotech.R;
import com.example.bibliotech.datos.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class libroresActivity extends AppCompatActivity {
    ImageView img_qr;
    Button btn_volver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sala_page_reservabook);  // Reemplaza con el nombre correcto de tu layout

        // Obtén datos de la intención
        User credentials = FireBaseActions.getUserAuth(this);
        String username = credentials.username;

        String nombreLibro = getIntent().getStringExtra("nombreLibro");
        String fechaDevolucion = getIntent().getStringExtra("fechaDevolucion");
        String fechaReservadia = getIntent().getStringExtra("fechaReservadia");
        String fechaReservames = getIntent().getStringExtra("fechaReservames");
        String fechaReservaano = getIntent().getStringExtra("fechaReservaano");

// Muestra los datos en tus vistas
        TextView user = findViewById(R.id.username);
        TextView nombreLibroTextView = findViewById(R.id.bk_name);
        TextView fechaDevolucionTextView = findViewById(R.id.day_dev);
        TextView fechaReservaTextView = findViewById(R.id.day_res);

        user.setText(username);
        nombreLibroTextView.setText(nombreLibro);
        fechaDevolucionTextView.setText(fechaDevolucion);

        fechaReservaTextView.setText(fechaReservadia + "/" + fechaReservames + "/" + fechaReservaano);
        img_qr = findViewById(R.id.qr);
        generateQR();

        btn_volver = findViewById(R.id.btn_volver);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the EditProfileActivity when the "editar" ImageView is clicked
                Intent intent = new Intent(libroresActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void generateQR() {
        String userID = FireBaseActions.getUserId();
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            BitMatrix matrix = writer.encode(userID, BarcodeFormat.QR_CODE, 400, 400);

            // Cambiar el color del fondo del código QR (en este caso, blanco)
            int bgColor = Color.WHITE;
            int fgColor = Color.rgb(83,121,164);

            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            // Cambiar colores en la imagen del código QR
            Bitmap coloredBitmap = changeBitmapColor(bitmap, bgColor, fgColor);

            img_qr.setImageBitmap(coloredBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap changeBitmapColor(Bitmap sourceBitmap, int bgColor, int fgColor) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        ColorFilter filter = new LightingColorFilter(bgColor, fgColor);
        paint.setColorFilter(filter);

        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }
}
