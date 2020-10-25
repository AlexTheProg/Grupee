package com.example.grupee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickFunction(View buttonView){
        EditText nameEditText = findViewById(R.id.personNameTextField);
        Log.i("InfoLog", "Hello there friends");
        Log.i("Values", nameEditText.getText().toString());

        Toast.makeText(this, nameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}