package www.starcom.com.jualanpraktis;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;

/**
 * Created by ADMIN on 12/02/2018.
 */

public class login extends AppCompatActivity implements View.OnClickListener {

    private static final String URL_LOGIN = "https://batammall.co.id/ANDROID/login.php";
    private final String TAG = this.getClass().getName();

    private Button btn_masuk,btn_daftar;
    private EditText loginEmail,loginPass;
    ProgressBar progressBar;
    String username, password;

    private akun akun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         progressBar = findViewById(R.id.progressBar);
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_pass);
        btn_masuk = findViewById(R.id.btn_masuk);
        btn_daftar = findViewById(R.id.btn_daftar);

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = loginEmail.getText().toString();
                password = loginPass.getText().toString();

                //validating inputs
                if (TextUtils.isEmpty(username)) {
                    loginEmail.setError("Email Belum Di isi");
                    loginEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    loginPass.setError("Password Belum Di isi");
                    loginPass.requestFocus();
                } else{
                    userLogin();
                }
            }
        });


        btn_daftar.setOnClickListener(this);

    }

    //Daftar
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(login.this,daftar.class);
        startActivity(intent);
        //getActivity().finish();
    }

    private void userLogin() {
        progressBar.setVisibility(View.VISIBLE);

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("response");
                            //JSONObject userJson = obj.getJSONObject("login");


                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String getObject = jsonObject.toString();
                                JSONObject object = new JSONObject(getObject);
                                loginuser user = new loginuser(
                                        object.getInt("id_customer"),
                                        object.getString("nama"),
                                        object.getString("email"),
                                        object.getString("no_hp"),
                                        object.getString("jk")
                                );
                                progressBar.setVisibility(View.GONE);
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                Toast.makeText(login.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                                //starting the profile activity


                        } catch (JSONException e) {
                            Toast.makeText(login.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(login.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
