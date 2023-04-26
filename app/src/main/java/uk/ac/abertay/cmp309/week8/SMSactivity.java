package uk.ac.abertay.cmp309.week8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SMSactivity extends AppCompatActivity {

    private EditText txtMobile;
    private EditText txtMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsactivity);

        txtMessage = findViewById(R.id.smstext);

        txtMobile = findViewById(R.id.smsPhone);





    }
    public void smsclickb(View view) {
       switch (view.getId()) {
         case R.id.smsbutton:
            sendtext();
            break;
       }
    }
    private void sendtext(){
        try{
            Intent smstext = new Intent(Intent.ACTION_SEND);
//        smstext.putExtra("address",new String[]{smstext.getAction().toString()});
//            sms.setData(Uri.parse("smsto:"));
            smstext.putExtra("sms_body",txtMessage.getText().toString());
            smstext.setType("vnd.android-dir/mms-sms");
            startActivity(smstext);


//            SmsManager smgr = SmsManager.getDefault();
//            smgr.sendTextMessage(txtMobile.getText().toString(),null,txtMessage.getText().toString(),null,null);
            Toast.makeText(SMSactivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(SMSactivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }


    }
}