package uk.ac.abertay.cmp309.week8;

import static uk.ac.abertay.cmp309.week8.Utils.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /* An array of all permissions we want ot check for this activity */
    String[] permissions = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE
    };

    ConnectivityManager connManager;

    TelephonyManager telephonyManager;

    PhoneStateListener phoneStateListener;
    boolean isConnected = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        // Create PhoneStateListener
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: {
                        Toast.makeText(MainActivity.this, "Phone state: IDLE", Toast.LENGTH_SHORT).show();
                        Log.d("PHONESTATE", "Phone state: IDLE");
                    }
                    break;
                    case TelephonyManager.CALL_STATE_RINGING: {
                        Toast.makeText(MainActivity.this, "Phone state: RINGING", Toast.LENGTH_SHORT).show();
                        Log.d("PHONESTATE", "Phone state: RINGING");
                    }
                    break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
                        Toast.makeText(MainActivity.this, "Phone state: OFFHOOK", Toast.LENGTH_SHORT).show();
                        Log.d("PHONESTATE", "Phone state: OFFHOOK");
                    }
                    break;
                    default: {
                        Toast.makeText(MainActivity.this, "Unknown phone state", Toast.LENGTH_SHORT).show();
                        Log.d("PHONESTATE", "Unknown phone state");
                    }
                    break;
                }
            }
        };




        //DOSE THIS CODE MEAN I DON'T NEED TO CHECK FOR PHONE AND SMS PERMISHONS?
        /* Check all permissions using the custom utility class and request them */
        if (!Utils.checkAllPermissions(this, permissions)) {
            requestPermissions(permissions, 0);
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_CALL_STATE);




        }

        /* Initialise the connectivity manager object, do this before starting any network operations */
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)

                .build();
        /* Register callback to monitor for changes */
        connManager.registerNetworkCallback(networkRequest, netCallback);

        /* Get active networks info and check if it's connected */
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        isConnected = (netInfo != null && netInfo.isConnected());
        if (isConnected) {
            /* Get network capabilities */
            Network net = connManager.getActiveNetwork();
            NetworkCapabilities netCaps = connManager.getNetworkCapabilities(net);


            if(netCaps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                Log.i(TAG,"connection is wifi");


            if(netCaps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                Log.i(TAG,"You are connected to the internet");
        } else {

            Log.e(TAG,"No connection!!");
            //return connManager.getActiveNetwork() !=null && connManager.getActiveNetworkInfo().isConnected();
        }
    }

    /* Button handler method for onClick property in the UI editor */
    public void buttonHandler(View view) {
        switch (view.getId()) {
            case R.id.btnView:
                startActivity(new Intent(MainActivity.this, ContactsListActivity.class));
                break;
            case R.id.btnSave:
                addContantToFirebase();
                break;
            case R.id.btnClear:
                clearFields();
                break;
        }
    }

    /* Clears all input fields */
    private void clearFields() {
        ((EditText) findViewById(R.id.etFirstName)).setText("");
        ((EditText) findViewById(R.id.etLastName)).setText("");
        ((EditText) findViewById(R.id.etEmail)).setText("");
        ((EditText) findViewById(R.id.etPhone)).setText("");
        ((EditText) findViewById(R.id.etPostcode)).setText("");
    }

    /* Adds contacts to Firestore */
    private void addContantToFirebase() {
        /* Construct a map of key-value pairs */
        Map<String, Object> contact = new HashMap<>();
        contact.put(FirestoreContact.KEY_FIRST, ((EditText) findViewById(R.id.etFirstName)).getText().toString());
        contact.put(FirestoreContact.KEY_LAST, ((EditText) findViewById(R.id.etLastName)).getText().toString());
        contact.put(FirestoreContact.KEY_EMAIL, ((EditText) findViewById(R.id.etEmail)).getText().toString());
        contact.put(FirestoreContact.KEY_PHONE, ((EditText) findViewById(R.id.etPhone)).getText().toString());
        contact.put(FirestoreContact.KEY_POSTCODE, ((EditText) findViewById(R.id.etPostcode)).getText().toString());


        /* Add a new document with a generated ID (name) */
        db.collection(FirestoreContact.COLLECTION_PATH).add(contact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }




    /* NOTE! This is just a normal variable declaration for Network Callback,
    * it is outside functions but inside the class.
    * This was done so to keep the top of the document tidy */


    ConnectivityManager.NetworkCallback netCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onLost(Network network) {

        }

        @Override
        public void onLosing(Network network, int maxMsToLive) {

        }

        @Override
        public void onUnavailable() {

        }

        @Override
        public void onAvailable(Network network) {

        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {

        }
    };

    /* Don't forget to unregister the callback when no longer needed */
    @Override
    protected void onDestroy() {
        connManager.unregisterNetworkCallback(netCallback);
        super.onDestroy();
    }


}
