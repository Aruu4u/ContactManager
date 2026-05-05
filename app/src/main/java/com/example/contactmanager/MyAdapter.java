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
    private OnContactClickListener listener;
    private OnContactLongClickListener longClickListener;

    // Interface for click callbacks
    public interface OnContactClickListener {
        void onContactClick(Contacts contact);
    }

    public interface OnContactLongClickListener {
        void onContactLongClick(Contacts contact);
    }

    // Constructor accepts contact list and click listener
    public MyAdapter(ArrayList<Contacts> contacts, OnContactClickListener listener, OnContactLongClickListener longClickListener) {
        this.contacts = contacts;
        this.listener = listener;
        this.longClickListener = longClickListener;
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
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    // Update contacts list and refresh RecyclerView
    public void setContacts(ArrayList<Contacts> contacts) {
        this.contacts = contacts;
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
