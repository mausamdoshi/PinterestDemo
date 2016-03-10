package com.exercise.pinterestdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.exercise.pinterestdemo.views.MyProgressDialog;

/**
 * @author mausam
 * Base Activity class that contains common methods which are available to inherit in
 * its child activity.
 * Parent Class for all other Activities in app
 *
 */
public class BaseActivity extends AppCompatActivity{

    protected MyProgressDialog progressDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    //Display custom progress bar
    protected void showProgressDialog() {

        progressDialog = new MyProgressDialog(this);
    }


    //Dismiss custom progress bar
    protected void dismissProgressDialog() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    //To Display Message in Toast
    public void showMessage(final String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

}
