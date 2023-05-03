package uk.ac.abertay.cmp309.week8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
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
    public class IncomingSmsReceiver extends AppCompatActivity {
        /** This method is called whenever there is a Broadcast with the action that matches the intent
         * filter associated with this receiver. */

        public void onReceive(Context context, Intent intent) {
            /* Get the bundle of extras from incoming intent. */
            Bundle bundle = intent.getExtras();
            /* Make sure it exists, just in case, to avoid crashes. */
            if(bundle != null)
            {
                /* Get SMS PDUs, which contain the text message data. */
                Object[] pdus = (Object[])bundle.get("pdus");
                /* Get message format. This will be used to extract message from PDUs. */
                String format = (String)bundle.get("format");
                /* Prepare an array to hold messages. The size of the array should match the number of PDUs. */
                SmsMessage[] messages = new SmsMessage[pdus.length];
                /* Extract message from each PDU. Itr is likely to be just 1 PDU/Message, but we need to
                 * do this in case multipart message is received. Try sending a long message (> 160 characters)
                 * to see it arrive in multiple parts here. */
                for(int i = 0; i < pdus.length; i++){
                    /* Create each message using PDU data and format. */
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i], format);
                    /* Extract message content as String */
                    String message = messages[i].getMessageBody();
                    /* Extract message address (i.e. phone number) as String */
                    String address = messages[i].getOriginatingAddress();
                    /* Display each message in a toast. */
                    Toast.makeText(context, "Received *"+message + "* FROM " + address, Toast.LENGTH_SHORT).show();
                }

                /* NOTE that we are not doing anything with the messages after we construct the array.
                 * This is just a demonstration of how we can catch the message broadcast and store the
                 * messages. If you want to do something with the messages, you'll can do it after this line.*/
            }
        }
    }
}