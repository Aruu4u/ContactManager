package com.example.contactmanager;

import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "contact_table")  // if we donot put this line then the name of the table will be the name of the class
public class Contacts extends BaseObservable implements Serializable {

    //each variable will represent a column in the table

    @ColumnInfo(name = "contact_id")
    @PrimaryKey(autoGenerate = true)   // Id field will be set as a Primary key
    private int id;

    // private int id;   if we do this then the column will be denoted by the name of the variable

   @ColumnInfo(name = "contact_name")
    private String name;

   @ColumnInfo(name = "contact_email")
    private String email;

   @ColumnInfo(name = "contact_number")
    private String number;

    private String description;

    private String profileImageUri;
    private boolean isFavorite;
    private String category;

    public Contacts() {
    }

    public Contacts(String name, String email,String number,String description) {


        this.name = name;
        this.email = email;
        this.number=number;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProfileImageUri() { return profileImageUri; }
    public void setProfileImageUri(String profileImageUri) { this.profileImageUri = profileImageUri; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
