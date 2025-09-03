package com.example.contactmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private MyAdapter myAdapter;
    private ArrayList<Contacts> contactsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainActivityClickHandlers handlers = new MainActivityClickHandlers(this);
        mainBinding.setClickHandler(handlers);

        ViewCompat.setOnApplyWindowInsetsListener(mainBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = mainBinding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Initialize adapter with click listener
        myAdapter = new MyAdapter(contactsArrayList, contact -> openContactDetailFragment(contact));
        recyclerView.setAdapter(myAdapter);

        // ViewModel to observe contact list
        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        viewModel.getAllContacts().observe(this, new Observer<List<Contacts>>() {
            @Override
            public void onChanged(List<Contacts> contacts) {
                contactsArrayList.clear();
                if (contacts != null) {
                    contactsArrayList.addAll(contacts);
                }
                myAdapter.notifyDataSetChanged();
            }
        });

        // Swipe to delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false; // no drag & drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contacts contactToDelete = contactsArrayList.get(viewHolder.getAdapterPosition());
                viewModel.deleteContact(contactToDelete);
            }
        }).attachToRecyclerView(recyclerView);


    }

    // Open fragment to show contact details
    private void openContactDetailFragment(Contacts contact) {
        ContactDetailFragment fragment = ContactDetailFragment.newInstance(contact);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)  // your fragment container ID in activity_main.xml
                .addToBackStack(null)
                .commit();
    }

    // ContactDetailFragment showing details and call/email options
    public static class ContactDetailFragment extends Fragment {

        private static final String ARG_CONTACT = "arg_contact";
        private Contacts contact;

        public ContactDetailFragment() {}

        public static ContactDetailFragment newInstance(Contacts contact) {
            ContactDetailFragment fragment = new ContactDetailFragment();
            Bundle args = new Bundle();
            args.putSerializable(ARG_CONTACT, contact);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                contact = (Contacts) getArguments().getSerializable(ARG_CONTACT);
            }


        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);

            TextView descriptionText = view.findViewById(R.id.textViewDescription);
            View callButton = view.findViewById(R.id.buttonCall);
            View emailButton = view.findViewById(R.id.buttonEmail);

            if (contact != null) {
                descriptionText.setText(contact.getDescription());
            }

            callButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getNumber()));
                startActivity(intent);
            });

            emailButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + contact.getEmail()));
                startActivity(intent);
            });

            return view;
        }
    }
}
