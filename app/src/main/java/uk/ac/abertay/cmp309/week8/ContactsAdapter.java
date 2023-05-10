package uk.ac.abertay.cmp309.week8;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/* This is the same adapter as in Lab6 but using FirestoreContact class instead of String[] */
public class ContactsAdapter extends ArrayAdapter<FirestoreContact> {


    public ContactsAdapter(Context context, ArrayList<FirestoreContact> contacts){
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Get the contacts data for this position. */
        FirestoreContact contact = getItem(position);
        /* Check if an existing view is being reused, otherwise inflate the view. */
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_layout, parent, false);
        }
        /* Lookup views. */
        TextView display_name = (TextView) convertView.findViewById(R.id.display_name);
        TextView display_email = (TextView) convertView.findViewById(R.id.display_email);
        TextView display_phone = (TextView) convertView.findViewById(R.id.display_phone);
        TextView display_postcode = (TextView) convertView.findViewById(R.id.display_postcode);

        /* Add the data to the template view. */
        display_name.setText(contact.getFirst()+" "+contact.getLast());
        display_email.setText(contact.getEmail());
        display_phone.setText(contact.getPhone());
        display_postcode.setText(contact.getPostcode());






        /* Return the completed view to render on screen. */
        return convertView;
    }
}