package com.example.contactmanager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class AddNewContactClickHandler {

    Contacts contact;
    Context context;
    MyViewModel myViewModel;
    boolean isEditing;

    public AddNewContactClickHandler(Contacts contact, Context context, MyViewModel myViewModel) {
        this(contact, context, myViewModel, false);
    }

    public AddNewContactClickHandler(Contacts contact, Context context, MyViewModel myViewModel, boolean isEditing) {
        this.contact = contact;
        this.context = context;
        this.myViewModel = myViewModel;
        this.isEditing = isEditing;
    }

    public void onSubmitBtnClicked(View view) {
        String name = contact.getName();
        String email = contact.getEmail();
        String number = contact.getNumber();
        String description = contact.getDescription();

        if (name == null || name.trim().isEmpty()) {
            Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (number == null || !number.matches("\\d{10}")) {
            Toast.makeText(context, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email != null && !email.trim().isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description != null && !description.trim().isEmpty()) {
            String[] words = description.trim().split("\\s+");
            if (words.length > 30) {
                Toast.makeText(context, "Description cannot exceed 30 words", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create new Contact object including description
        Contacts newContact = new Contacts(
                contact.getName(),
                contact.getEmail(),
                contact.getNumber(),
                contact.getDescription()
        );
        newContact.setId(contact.getId());

        if (isEditing) {
            myViewModel.updateContact(newContact);
        } else {
            // Add new contact to ViewModel (which should save it in DB or list)
            myViewModel.addNewContact(newContact);
        }

        // Start MainActivity after saving contact
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
