package com.example.contactmanager;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



//Repository ek mediator hota hai between your data sources (Room DB, Firebase, API, etc.) and the rest of your app (like ViewModel or Activity).
//Aapka code clean, testable, and maintainable hota hai.
// Easily switch kar sakte ho — aaj Room DB use ho raha hai, kal API se data lana ho — bas Repository me change karo, baaki code same rahega.
//Socho aapka app "Contacts" ka data, Room database me rakhta hai.
//Activity ko sirf yeh chahiye: “Mujhe contacts de do”
//Ab vo yeh na soche ki contacts DB se aa rahe hain ya API se — ye kaam Repository karegi.

public class Repository {

    private final  ContactDAO contactDAO;
//    contactDAO: Room database ka object, jisse hum data fetch/insert/delete karenge. iske baad constructor or fir DOA ke methods call krna


// agar DB operations Main UI thread pe chale toh UI crash ho sakta hai isiliye DB operations ko ek background thread pe run karate hai.
//executor: Background me kaam karne ke liye (database operations (insert,delete) UI thread pe nahi hone chahiye).
//handler: UI ko update karne ke liye (agar kabhi zarurat pade).

    ExecutorService executor;
    Handler handler;


    public Repository(Application application) { // construct banaya : jabh class call hogi tabh ye run hoga

        ContactDatabase db = ContactDatabase.getInstance(application);
        this.contactDAO = db.getContactDAO();

        //used for background database operations
        executor = Executors.newSingleThreadExecutor(); //Ek background thread banaya — ek time pe ek kaam karega, jisse crash aur bugs avoid hote hain.

        // used for updating the ui
        handler= new Handler(Looper.getMainLooper()); //UI thread se interact karne ke liye Handler use kiya gaya — agar kabhi background se UI update karna ho to.


    }






    // add all the methods in the contactDAO interface
// and pass entity class obj  as parameter
    public void addContact(Contacts cnt){


        executor.execute(new Runnable() {
            @Override
            public void run() {
                contactDAO.insert(cnt);}
                                        });

    }




    public void deleteContact(Contacts cnt){

        executor.execute(new Runnable() {  //runnable : execute task on separate threads
            @Override
            public void run() {
                contactDAO.delete(cnt);
                }
                                         });

    }



    public LiveData<List<Contacts>> getAllContacts(){
        return contactDAO.getAllContacts();
    }




    }

