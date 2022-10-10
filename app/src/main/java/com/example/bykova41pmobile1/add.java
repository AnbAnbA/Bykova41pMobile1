package com.example.bykova41pmobile1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class add extends AppCompatActivity {

    EditText Name_of_MI, Manufacturers, Manufacturer_country, Price_MI;
    ImageView ph;
    Connection connection;
    String ConnectionResult = "";
    String Img = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mi);
        Name_of_MI = (EditText) findViewById(R.id.Name_of_MI);
        Manufacturers = (EditText) findViewById(R.id.Manufacturers);
        Manufacturer_country = (EditText) findViewById(R.id.Manufacturer_country);
        Price_MI = (EditText) findViewById(R.id.Price_MI);
        ph = (ImageView) findViewById(R.id.ph);
    }

    @Override
    protected void onActivityResult(int request, int result, @Nullable Intent data) {
        try {
            super.onActivityResult(request, result, data);
            if (request == 1 && data != null && data.getData() != null) {
                if (result == RESULT_OK) {
                    Log.d("MyLog", "Image URI : " + data.getData());
                    ph.setImageURI(data.getData());
                    Bitmap bitmap = ((BitmapDrawable) ph.getDrawable()).getBitmap();
                    encodeImg(bitmap);
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(add.this,"Что-то не так...", Toast.LENGTH_LONG).show();
        }
    }
    public String encodeImg(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Img = Base64.getEncoder().encodeToString(bytes);
            return Img;
        }
        return "";
    }

    public void add(View view) {
        Name_of_MI = (EditText) findViewById(R.id.Name_of_MI);
        Manufacturers = (EditText) findViewById(R.id.Manufacturers);
        Manufacturer_country = (EditText) findViewById(R.id.Manufacturer_country);
        Price_MI = (EditText) findViewById(R.id.Price_MI);


        try {
            ConnectionHelper connectionHelpers = new ConnectionHelper();
            connection = connectionHelpers.connectionClass();
            if (connection != null) {
                String query = "INSERT INTO Musical_Instrument (Name_of_MI, Manufacturers, Manufacturer_country, Price_MI, Image) VALUES ('" + Name_of_MI.getText() + "', '" + Manufacturers.getText() + "', '" + Manufacturer_country.getText() + "', '" + Price_MI.getText() + "' , '" + Img + "')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                Toast.makeText(this, "Успешное добавление записи!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex)
        {
            Log.e("Error", ex.getMessage());
        }
    }

    public void OnClickImage(View view) {
        try {
            Intent intentChooser = new Intent();
            intentChooser.setType("image/*");
            intentChooser.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentChooser, 1);
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Что-то не так...", Toast.LENGTH_LONG).show();
        }
    }

}


