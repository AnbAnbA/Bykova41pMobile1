package com.example.bykova41pmobile1;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Base64;

public class up extends AppCompatActivity {

    Connection connection;
    View v;
    EditText Name_of_MI, Manufacturers, Manufacturer_country, Price_MI;
    ImageView ph;
    MI mi;
    String Img=null;
    String ConnectionResult="";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.up_mi);
        Name_of_MI = (EditText) findViewById(R.id.Name_of_MI);
        Manufacturers = (EditText) findViewById(R.id.Manufacturers);
        Manufacturer_country = (EditText) findViewById(R.id.Manufacturer_country);
        Price_MI = (EditText) findViewById(R.id.Price_MI);
        ph = (ImageView) findViewById(R.id.ph);
        mi = getIntent().getParcelableExtra("Musical_Instrument");
        Name_of_MI.setText(mi.getName_of_MI());
        Manufacturers.setText(mi.getManufacturers());
        Manufacturer_country.setText(mi.getManufacturer_country());
        Price_MI.setText(Integer.toString(mi.getPrice_MI()));
        ph.setImageBitmap(getImgBitmap(mi.getImage()));
        v = findViewById(com.google.android.material.R.id.ghost_view);

    }

    protected void onActivityResult(int request, int result, @Nullable Intent data) {
        try {
            super.onActivityResult(request, result, data);
            if (request == 1 && data != null && data.getData() != null) {
                if (result == RESULT_OK) {
                    Log.d("MyLog", "Image URI : " + data.getData());

                    ph.setImageURI(data.getData());
                    Bitmap bitmap = ((BitmapDrawable) ph.getDrawable()).getBitmap();
                    toString(bitmap);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Что-то не так...", Toast.LENGTH_LONG).show();
        }
    }
    public String toString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Img = Base64.getEncoder().encodeToString(b);
            return Img;
        }
        return "";
    }

    private Bitmap getImgBitmap(String encodedImg) {
        if(encodedImg!=null&& !encodedImg.equals("null")) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(up.this.getResources(),R.drawable.ph);
    }

    public void edit(View view) { //Изменение данных
        if(Name_of_MI.getText().length() == 0 || Manufacturers.getText().length() == 0 || Manufacturer_country.getText().length() == 0|| Price_MI.getText().length() == 0)
        {
            Toast.makeText(this, "Поля не заполненны!", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            try {
                ConnectionHelper connectionHelpers = new ConnectionHelper();
                connection = connectionHelpers.connectionClass();
                if (connection != null) {
                    String query = "UPDATE Musical_Instrument SET Name_of_MI = '" + Name_of_MI.getText().toString() + "' , Manufacturers = '" + Manufacturers.getText().toString() + "', Manufacturer_country = '" + Manufacturer_country.getText().toString() + "', Price_MI = '" + Price_MI.getText().toString() + "' , Image = '" + Img + "' where ID = '" + mi.getID() + "'";
                    Statement statement = connection.createStatement();
                    Toast.makeText(this, "Изменение данных: успешно!", Toast.LENGTH_LONG).show();
                    statement.executeUpdate(query);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    ConnectionResult = "Check Connection";
                }
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
            }
        }
    }

    public void del(View view) {
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if (connection != null)
            {
                String str = "DELETE FROM Musical_Instrument WHERE ID = "+mi.getID()+"";
                Statement statement = connection.createStatement();
                Toast.makeText(this, "Удаление данных: успешно!", Toast.LENGTH_LONG).show();
                statement.executeUpdate(str);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception ex)
        {
            Log.e("Error", ex.getMessage());
        }
    }
    public void OnClickImg(View view) {
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



