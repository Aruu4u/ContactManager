package com.example.contactmanager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class AddNewContactClickHandler {

    Contacts contact;
    Context context;
    MyViewModel myViewModel;

    public AddNewContactClickHandler(Contacts contact, Context context, MyViewModel myViewModel) {
        this.contact = contact;
        this.context = context;
        this.myViewModel = myViewModel;
    }

    public void onSubmitBtnClicked(View view) {
        // Validate that none of the fields are empty or null
        if (contact.getName() == null || contact.getName().isEmpty() ||
               // contact.getEmail() == null || contact.getEmail().isEmpty() ||
                contact.getNumber() == null || contact.getNumber().isEmpty()
                //  || contact.getDescription() == null || contact.getDescription().isEmpty()
                         )
        {

            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new Contact object including description
        Contacts newContact = new Contacts(
                contact.getName(),
                contact.getEmail(),
                contact.getNumber(),
                contact.getDescription()
        );

        // Add new contact to ViewModel (which should save it in DB or list)
        myViewModel.addNewContact(newContact);

        // Start MainActivity after saving contact
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
