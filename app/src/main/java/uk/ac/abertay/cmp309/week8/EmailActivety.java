package uk.ac.abertay.cmp309.week8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class EmailActivety extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);
        String emailname = getIntent().getStringExtra("ename");

        EditText email = findViewById(R.id.EmailAddress);

        email.setText(emailname);
    }
}