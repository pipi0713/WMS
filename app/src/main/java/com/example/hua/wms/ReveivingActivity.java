package com.example.hua.wms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import utils.AccessToken;

public class ReveivingActivity extends AppCompatActivity {
    Intent intent=new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String myToken = AccessToken.getInstance().getToken();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveiving);
//        Toast.makeText(ReveivingActivity.this,myToken,Toast.LENGTH_LONG).show();

    }
    public void bt_receveing(View view){

        intent.setClass(ReveivingActivity.this,Reveiving2Activity.class);
        ReveivingActivity.this.startActivity(intent);

    }
    public void inspection_bt(View view){
        intent.setClass(ReveivingActivity.this,InspectionActivity.class);
        ReveivingActivity.this.startActivity(intent);

    }

}
