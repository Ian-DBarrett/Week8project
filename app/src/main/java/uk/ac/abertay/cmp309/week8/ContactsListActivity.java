package uk.ac.abertay.cmp309.week8;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactsListActivity extends AppCompatActivity implements EventListener<QuerySnapshot> {

    private ListView list;
    CollectionReference contacts = FirebaseFirestore.getInstance().collection(FirestoreContact.COLLECTION_PATH);
    ArrayList<FirestoreContact> data = new ArrayList<>();
    ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        /* Get reference to list view */
        list = findViewById(R.id.list_contacts);


        //@Override
       // public Dialog select(Bundle savedInstanceState)  {
        //    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //}

        /* Set on item click listener to output the clicked contact's details to log */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                //SMS TEXT hear
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactsListActivity.this);
                builder.setMessage("text or call")
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                // START THE GAME!
                                FirestoreContact contact = (FirestoreContact) parent.getAdapter().getItem(position);
                                Log.d(TELEPHONY_SERVICE, contact.getPhone());

                                Uri num = Uri.parse("tel:+" + contact.getPhone());
                                Intent call = new Intent(Intent.ACTION_DIAL, num);
                                //MAKE A CALL HEAR
                                startActivity(call);
                            }
                        })
                        .setNegativeButton("text", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(ContactsListActivity.this, SMSactivity.class));
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();



            }
        });



        /* Add a listener to the entire collection of contact which will notify of changes */
        contacts.addSnapshotListener(this);
        /* Populate the contact list from Firestore data */
        populateList();
    }

    private void populateList(){
        /* Get all contact documents in collection */
        contacts.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(Utils.TAG, document.getId() + " => " + document.getData());
                                /* Get contact as FirestoreContact object */
                                FirestoreContact contact = document.toObject(FirestoreContact.class);
                                /* Set object's ID from document name (id) */
                                contact.setID(document.getId());
                                /* Add to list of contacts */
                                data.add(contact);
                            }
                            /* Initialise the adapter with the list of contacts */
                            adapter = new ContactsAdapter(ContactsListActivity.this, data);
                            /* Attach the adapter to our list view. */
                            list.setAdapter(adapter);
                        } else {
                            Log.w(Utils.TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /* Process updates for any of the contacts in the Firestore "contacts" collection */
    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        /* If exception occurs, don't try to do anything else, just display the error and return */
        if (e != null) {
            Log.e(Utils.TAG, "Listen failed.", e);
            return;
        }

        /* Update data from Firestore, then list via adapter if data changes*/
        if(data != null && adapter != null){ /* important checks to avoid crashes */
            /* If there is a change these should not be bull, it may be empty if
            * all documents have been deleted form the collection. */
            if(queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty()) {
                /* Clears adapter and creates a new data list */


                adapter.clear();
                data = new ArrayList<>();
                /* Gets all documents in the affected collection */
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //Log.d(Utils.TAG, document.getId() + " => " + document.getData());
                    /* Same as before, get the data for each item */
                    FirestoreContact contact = document.toObject(FirestoreContact.class);
                    contact.setID(document.getId());
                    /* Add contact to the new list */
                    data.add(contact);
                }
                /* Add all data to adapter */
                adapter.addAll(data);
                /* Tell the adapter to redraw the listView with new dataset */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }
    }
}
