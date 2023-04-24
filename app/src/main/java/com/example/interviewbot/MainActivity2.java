package com.example.interviewbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    Button submit;
    EditText position, company_name,user_name,desc_content ;
    Spinner degree_spinner;
    String userName,posName,degreeName,companyName,final_string,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_page);
        submit = findViewById(R.id.button);
        user_name = findViewById(R.id.editTextTextPersonName1d);
        position =findViewById(R.id.pos_spinner);
        company_name =findViewById(R.id.spinner);
        degree_spinner =  findViewById(R.id.spinner1);
        desc_content = findViewById(R.id.editTextTextMultiLinef);
        ArrayAdapter<CharSequence> degree_arr = ArrayAdapter.createFromResource(this, R.array.degree_list, R.layout.spinner_item);
        degree_arr.setDropDownViewResource(R.layout.spinner_selected_item);
        degree_spinner.setAdapter(degree_arr);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position.length()==0){
                    position.setError("Please enter your position");
                }
                if(company_name.length()==0){
                    company_name.setError("Please enter your company name");
                }
                if (user_name.length()==0) {
                    user_name.setError("Please enter your name");
                }
                if(position.length()!=0 && company_name.length()!=0 && user_name.length()!=0){
                    userName = user_name.getText().toString();
                    posName = user_name.getText().toString();
                    companyName = company_name.getText().toString();
                    degreeName = degree_spinner.getSelectedItem().toString();
                    description = desc_content.getText().toString();
                    final_string = "My name is "+ userName+ " and I am currently pursuing " + degreeName+" .I want to prepare for "+posName+" at "+companyName +" I am " + description+
                " Can you behave like an Interviewer and ask me questions to help me to prepare";

                    Intent i = new Intent(getApplicationContext(), ChatbotActivity.class);
                    i.putExtra("final_string",final_string);
                    startActivity(i);
                }
            }
        });

    }
}