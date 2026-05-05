
package com.example.contactmanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private MyAdapter myAdapter;
    private ArrayList<Contacts> contactsArrayList = new ArrayList<>();
    private static final int REQUEST_DEFAULT_SMS = 200;

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



        // ✅ Runtime permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        }

        RecyclerView recyclerView = mainBinding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        myAdapter = new MyAdapter(contactsArrayList, contact -> openContactDetailFragment(contact), contact -> {
            Intent intent = new Intent(MainActivity.this, AddNewContactActivity.class);
            intent.putExtra("contact_to_edit", contact);
            startActivity(intent);
        });
        recyclerView.setAdapter(myAdapter);

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contacts contactToDelete = contactsArrayList.get(viewHolder.getAdapterPosition());
                viewModel.deleteContact(contactToDelete);
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton sosButton = findViewById(R.id.fab_sos);
        sosButton.setOnClickListener(v -> sendSOS());


    }

    // ✅ Handle result of default SMS selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DEFAULT_SMS) {
            String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this);

            if (defaultSmsApp != null && defaultSmsApp.equals(getPackageName())) {
                Toast.makeText(this, "App set as default SMS app!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not set as default SMS app", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendSOS() {
        LocationHelper locationHelper = new LocationHelper(this);
        locationHelper.getCurrentLocation(location -> {
            if (location != null) {
                String message = "🚨 SOS! I need help.\nMy location: "
                        + "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();

                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp"); // ✅ only WhatsApp
                    startActivity(sendIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "WhatsApp not installed!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void openContactDetailFragment(Contacts contact) {
        ContactDetailFragment fragment = ContactDetailFragment.newInstance(contact);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

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


