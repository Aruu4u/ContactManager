package com.example.contactmanager;


import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MainActivityClickHandlers {
    Context cntx;
    public MainActivityClickHandlers(Context cntx) {
        this.cntx = cntx;
    }

    public void onFABClicked(View view){
        Intent i = new Intent(view.getContext(),AddNewContactActivity.class);

        cntx.startActivity(i);
}

    }