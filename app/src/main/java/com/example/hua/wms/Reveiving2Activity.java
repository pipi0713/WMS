package com.example.hua.wms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Reveiving2Activity extends AppCompatActivity {
    Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveiving2);
    }
    public void fullcollection(View view){

        intent.setClass(Reveiving2Activity.this,Reveiving3Activity.class);
        Reveiving2Activity.this.startActivity(intent);
    }
    public void collection(View view){
        intent.setClass(Reveiving2Activity.this,Reveiving3Activity.class);
        Reveiving2Activity.this.startActivity(intent);

    }
}
