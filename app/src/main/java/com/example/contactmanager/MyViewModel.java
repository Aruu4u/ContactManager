package com.example.contactmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MyViewModel extends AndroidViewModel {                        //if you need to use context inside your viewmodel you shoulld use androidviewmodel
                                                                           // bcoz it contains the application context
                                    //Repository ko Application context chahiye hota hai, isliye AndroidViewModel use kiya gaya hai.




  private Repository myRepository;                            //myRepository → Data lane/insert/delete karne ke liye
                                                              // allContacts → Sare contacts LiveData me rakhe gaye hain (observe karne ke liye)
    private LiveData<List<Contacts>> allContacts;


    public MyViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = new Repository(application);//Jab ViewModel banega, to Repository ka object create karega (aur usme Application context dega).
    }


    public LiveData<List<Contacts>> getAllContacts(){
        allContacts = myRepository.getAllContacts();//Repository se LiveData<List<Contacts>> le raha hai. UI/Fragment is method ko call karke LiveData observe karega.
        return allContacts;
    }

    public void addNewContact(Contacts cnt){
        myRepository.addContact(cnt);
    }//Repository me contact insert karwane ka method — ye background thread me chalega.

    public void deleteContact(Contacts cnt){
        myRepository.deleteContact(cnt);
    } //

    public void updateContact(Contacts cnt){
        myRepository.updateContact(cnt);
    }


}
