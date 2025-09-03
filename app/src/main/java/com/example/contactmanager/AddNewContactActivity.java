package com.example.contactmanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.contactmanager.databinding.ActivityAddNewContactBinding;

public class AddNewContactActivity extends AppCompatActivity {

    private ActivityAddNewContactBinding binding;
    private AddNewContactClickHandler handler;
    private Contacts contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        // Set content view and bind
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_contact);

        // Apply system window insets padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize an empty Contacts object for data binding
        contacts = new Contacts();

        // Get your ViewModel
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Create click handler, pass contacts, context, and ViewModel
        handler = new AddNewContactClickHandler(contacts, this, myViewModel);

        // Bind variables to layout
        binding.setContact(contacts);
        binding.setClickhandler(handler);
    }
}
