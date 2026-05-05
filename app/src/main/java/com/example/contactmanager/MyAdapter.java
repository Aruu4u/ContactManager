package com.example.contactmanager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanager.databinding.ContactListitemBinding;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder> {

    private ArrayList<Contacts> contacts;
    private ArrayList<Contacts> contactsFull; // For searching
    private OnContactClickListener listener;
    private OnContactLongClickListener longClickListener;

    // Interface for click callbacks
    public interface OnContactClickListener {
        void onContactClick(Contacts contact);
    }

    public interface OnContactLongClickListener {
        void onContactLongClick(Contacts contact);
    }

    public interface OnStarClickListener {
        void onStarClick(Contacts contact);
    }

    private OnStarClickListener starClickListener;

    // Constructor accepts contact list and click listener
    public MyAdapter(ArrayList<Contacts> contacts, OnContactClickListener listener, OnContactLongClickListener longClickListener, OnStarClickListener starClickListener) {
        this.contacts = contacts;
        this.contactsFull = new ArrayList<>(contacts);
        this.listener = listener;
        this.longClickListener = longClickListener;
        this.starClickListener = starClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactListitemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_listitem,
                parent,
                false
        );
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contacts contact = contacts.get(position);
        holder.contactListitemBinding.setContact(contact);

        // Set click listener on item view
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactClick(contact);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onContactLongClick(contact);
                return true;
            }
            return false;
        });

        // Set star state
        if (contact.isFavorite()) {
            holder.contactListitemBinding.starImageView.setImageResource(R.drawable.ic_star_24);
        } else {
            holder.contactListitemBinding.starImageView.setImageResource(R.drawable.ic_star_outline_24);
        }

        // Set star click listener
        holder.contactListitemBinding.starImageView.setOnClickListener(v -> {
            if (starClickListener != null) {
                starClickListener.onStarClick(contact);
            }
        });

        // Set avatar image
        if (contact.getProfileImageUri() != null && !contact.getProfileImageUri().isEmpty()) {
            holder.contactListitemBinding.avatarImageView.setImageURI(android.net.Uri.parse(contact.getProfileImageUri()));
        } else {
            holder.contactListitemBinding.avatarImageView.setImageResource(R.drawable.baseline_person_pin_24);
        }
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    // Update contacts list and refresh RecyclerView
    public void setContacts(ArrayList<Contacts> contacts) {
        this.contacts = contacts;
        this.contactsFull = new ArrayList<>(contacts);
        notifyDataSetChanged();
    }

    // Filter logic for search
    public void filter(String text) {
        contacts.clear();
        if (text.isEmpty()) {
            contacts.addAll(contactsFull);
        } else {
            text = text.toLowerCase();
            for (Contacts item : contactsFull) {
                if (item.getName().toLowerCase().contains(text) || item.getNumber().contains(text)) {
                    contacts.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    // ViewHolder class with data binding reference
    class ContactViewHolder extends RecyclerView.ViewHolder {
        private ContactListitemBinding contactListitemBinding;

        public ContactViewHolder(@NonNull ContactListitemBinding binding) {
            super(binding.getRoot());
            this.contactListitemBinding = binding;
        }
    }
}
