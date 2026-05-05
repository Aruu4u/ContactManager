package com.example.contactmanager;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert
    void insert(Contacts cnt);

    @Delete
    void delete(Contacts cnt);

    @androidx.room.Update
    void update(Contacts cnt);

    @Query("SELECT * FROM contact_table ORDER BY isFavorite DESC, contact_name ASC")// this is a custom query which shows the item of list @Query is used for custom queries
    LiveData<List<Contacts>> getAllContacts();  // return type should be a list of Contact objects
    // WE USE LIVE DATA, now the room library ensures that data is observed by the REPOSITORY or ViewModel allowing real time update to ui.
    //(IMPLEMENT THE DEPENDENCIES)



}
