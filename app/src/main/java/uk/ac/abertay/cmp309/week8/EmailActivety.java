package uk.ac.abertay.cmp309.week8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class EmailActivety extends AppCompatActivity {
    private EditText email;

    private EditText emailsub;

    private EditText emailimput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);
        String emailname = getIntent().getStringExtra("ename");

        EditText email = findViewById(R.id.EmailAddress);
        EditText emailsub = findViewById(R.id.emailsubject);
        EditText emailimput = findViewById(R.id.emailimput);

        int findViewById = (R.id.buttonE);
        email.setText(emailname);


        }

        public void onsend() {
            String to = email.toString();
            String subject = emailsub.getText().toString();
            String message = emailimput.getText().toString();

            Intent emailsend = new Intent(Intent.ACTION_SEND);
            emailsend.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            emailsend.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailsend.putExtra(Intent.EXTRA_TEXT, message);

//need this to prompts email client only
            emailsend.setType("message/rfc822");

            startActivity(Intent.createChooser(emailsend, "Choose an Email client :"));
        }
    public void eclickb(View view) {
        switch (view.getId()) {
            case R.id.buttonE:
                onsend();
                break;
        }
    }
}