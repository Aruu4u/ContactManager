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

        // Check if we are editing an existing contact
        boolean isEditing = false;
        if (getIntent().hasExtra("contact_to_edit")) {
            contacts = (Contacts) getIntent().getSerializableExtra("contact_to_edit");
            isEditing = true;
        } else {
            contacts = new Contacts();
        }

        // Get your ViewModel
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Create click handler, pass contacts, context, and ViewModel
        handler = new AddNewContactClickHandler(contacts, this, myViewModel, isEditing);

        // Bind variables to layout
        binding.setContact(contacts);
        binding.setClickhandler(handler);

        // Initialize word count if editing
        updateWordCount(contacts.getDescription());

        // Add text watcher for live word count
        binding.textInputEditTextDescription.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateWordCount(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void updateWordCount(String text) {
        int wordCount = 0;
        if (text != null && !text.trim().isEmpty()) {
            wordCount = text.trim().split("\\s+").length;
        }
        binding.wordCountTextView.setText(wordCount + "/30 words");
        if (wordCount > 30) {
            binding.wordCountTextView.setTextColor(android.graphics.Color.RED);
        } else {
            binding.wordCountTextView.setTextColor(android.graphics.Color.parseColor("#454545"));
        }
    }
}
