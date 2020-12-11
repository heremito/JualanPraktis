package www.starcom.com.jualanpraktis.utils;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 *
 *  Shofwan
 *  2020
 *
 * **/
public class CustomProgressDialog {

    private Activity activity;
    private ProgressDialog progressDialog;

    public CustomProgressDialog(Activity activity){
        this.activity=activity;
    }

    public  void progress(String title, String message){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void dismiss(){
        progressDialog.dismiss();
    }


}
