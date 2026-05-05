package com.example.contactmanager;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities ={Contacts.class} , version = 5)  // define the entities in the project , here we have only one entity i.e Contacts.class
public abstract class ContactDatabase extends RoomDatabase { // make an abstract class extend it with RoomDatabase


    public abstract ContactDAO getContactDAO();  // linking the DAO to this  database class (calling the Dao interface)



    //Singleton Pattern :- to avoid creation of mutliple instances/object of this class which will lead to perfomance issue , memory issue

    private static ContactDatabase dbinstance; // jis class ko singleton bana rahe hai uska iklauta jo instance/object hota hai wo store karega ye variable jiska data type ussi class ka hoga jisko singleton bana rahe hai

    public static synchronized ContactDatabase getInstance(Context ctx) {
        //method will return ContactDatabse type , Synchronized is used: yeh thread safety ke liye hota hai agar multiple thread bante hai or isko ek sath call karte hai tabh bhi sirf ek hi instance banega

        if(dbinstance == null) //Check karta hai ki kya pehle se koi instance bana hai? Agar nahi bana (null hai), tab naya instance banayenge.


            {
            dbinstance=Room.databaseBuilder(
                    ctx.getApplicationContext(),
                    ContactDatabase.class,
                    "contacts_db").fallbackToDestructiveMigration().build();
        }


//        Ye Room.databaseBuilder() function  naya Room database create karta hai.
//        ctx.getApplicationContext() → Application context pass kar rahe ho (memory leak avoid karne ke liye).
//        ContactDatabase.class → Batata hai kis class ka database banana hai.
//        "contacts_db" → Database ka naam (aapke app ke internal storage me is naam ka DB banega).
//          .fallbackToDestructiveMigration()
//       Agar aap future me database version change karte ho, aur Room ko migration nahi milti, to ye purani database ko delete karke nayi banata hai.
//       Isse data delete ho jaata hai! Ye development ke time theek hai, lekin production me caution se use karte hain.

        return dbinstance;  //Finally, banaya hua ya existing dbinstance return karta hai.

    }

}