package com.example.bykova41pmobile1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    View v;
    Connection connection;
    String ConnectionResult = "";
    List<MI> data;
    ListView listView;
    Adapter pAdapter;

    EditText ser;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = findViewById(com.google.android.material.R.id.ghost_view);
        GetTextFromSQL(v);
        Triage();
    }

    private void Triage() {
        try {
            spinner = findViewById(R.id.spinner);
            List<String> list = new ArrayList<String>();
            list.add("Без");
            list.add("По производителю");
            list.add("По цене");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            String st = null;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    switch (position) {
                        case 0: {
                            sorting(st);

                        }
                        break;
                        case 1: {
                            sortingManufacturers(st);

                        }
                        break;
                        case 2: {
                            sortingPrice(st);
                        }
                        break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,"Что-то не так", Toast.LENGTH_LONG).show();
        }
    }

    private void sortingPrice(String st) {
        try {
            st = "Select * From Musical_Instrument ORDER BY Price";
            selectSort(st);
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,"Что-то так...", Toast.LENGTH_LONG).show();
        }
    }

    private void selectSort(String st) {
        data = new ArrayList<MI>();
        listView = findViewById(R.id.MI_data);
        pAdapter = new Adapter(MainActivity.this, data);
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connection = connectionHelper.connectionClass();
            if (connection != null)
            {
                String query = "Select * From Musical_Instrument";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next())
                {
                    MI tempMask = new MI
                            (resultSet.getInt("ID"),
                                    resultSet.getString("Name_of_MI"),
                                    resultSet.getString("Manufacturers"),
                                    resultSet.getString("Manufacturer_country"),
                                    resultSet.getInt("Price"),
                                    resultSet.getString("Image")
                            );
                    data.add(tempMask);
                    pAdapter.notifyDataSetInvalidated();
                }
                connection.close();
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        enterMobile();
    }

    private void sortingManufacturers(String st) {
        try {
            st = "Select * From Musical_Instrument ORDER BY Manufacturers";
            selectSort(st);
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,"Что-то не так...", Toast.LENGTH_LONG).show();
        }
    }

    private void sorting(String st) {
        try {
            st = "Select * From Musical_Instrument";
            selectSort(st);
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,"Что-то не так...", Toast.LENGTH_LONG).show();
        }
    }

    private void enterMobile() {
        pAdapter.notifyDataSetInvalidated();
        listView.setAdapter(pAdapter);
    }

    private void GetTextFromSQL(View v) {
        data = new ArrayList<MI>();
        listView = findViewById(R.id.MI_data);
        pAdapter = new Adapter(MainActivity.this, data);

        try {
            ConnectionHelper connectionHelper=new ConnectionHelper();
            connection=connectionHelper.connectionClass();

            if(connection!=null) {
                String query = "Select *From Musical_Instrument";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    MI tempMask=new MI
                            (resultSet.getInt("ID"),
                                    resultSet.getString("Name_of_MI"),
                                    resultSet.getString("Manufacturers"),
                                    resultSet.getString("Manufacturer_country"),
                                    resultSet.getInt("Price"),
                                    resultSet.getString("Image")
                            );
                    data.add(tempMask);
                    pAdapter.notifyDataSetInvalidated();
                }
                connection.close();
            }
            else{
                ConnectionResult="Check Connection";
            }
        }
        catch(Exception ex){
            Log.e(ConnectionResult, ex.getMessage());
            Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show();
        }
        enterMobile();
    }

    public void add(View view) {
        Intent intent = new Intent(this, add.class);
        startActivity(intent);
    }


}