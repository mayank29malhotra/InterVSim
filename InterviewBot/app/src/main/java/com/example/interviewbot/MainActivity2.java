package com.example.interviewbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_page);

        Spinner degree_spinner =  findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> degree_arr = ArrayAdapter.createFromResource(this, R.array.degree_list, R.layout.spinner_item);
        degree_arr.setDropDownViewResource(R.layout.spinner_selected_item);
        degree_spinner.setAdapter(degree_arr);

    }
}