package www.starcom.com.jualanpraktis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

/**
 * Created by ADMIN on 12/02/2018.
 */

public class daftar extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();

    private EditText nama,email,no_hp,password,ulangpass ;
    private RadioGroup jenis_kelamin;
    private RadioButton laki,perempuan;
    private Button btn_daftar ;
    String jk = "" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar");

        nama = findViewById(R.id.input_nama);
        email = findViewById(R.id.input_email);
        no_hp = findViewById(R.id.input_nohp);
        password = findViewById(R.id.input_pass);
        ulangpass = findViewById(R.id.input_ulang);
        btn_daftar = findViewById(R.id.btn_daftar);

        jenis_kelamin = findViewById(R.id.RG);
        laki = findViewById(R.id.laki);
        perempuan = findViewById(R.id.perempuan);

        UlangPass();
        btn_daftar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (nama.getText().toString().equals("")) {
            nama.setError("Nama belum di isi");
            nama.requestFocus();
        } else if (email.getText().toString().equals("")) {
            email.setError("Email belum di isi");
            email.requestFocus();
        } else if (no_hp.getText().toString().equals("")) {
            no_hp.setError("No Hp belum di isi");
            no_hp.requestFocus();
        } else if (password.getText().toString().equals("")) {
            password.setError("Password belum di isi");
            password.requestFocus();
        } else if (ulangpass.getText().toString().equals("")) {
            ulangpass.setError("Password belum di isi");
        } else if (!ulangpass.getText().toString().equals(password.getText().toString())) {
            ulangpass.setError("Password salah");
        } else if (jenis_kelamin.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Anda Belum Memilih Cara Memperoleh Informasi", Toast.LENGTH_LONG).show();
        }  else {
            Daftar();
        }
    }

    public void KosongField(){
        nama.setText("");
        email.setText("");
        no_hp.setText("");
        password.setText("");
        ulangpass.setText("");
        jenis_kelamin.check(0);

    }

    public void UlangPass(){

        ulangpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String Password = password.getText().toString();
                if (!ulangpass.getText().toString().equals(Password)){
                    ulangpass.setError("Password Salah");
                }
            }
        });
    }

    public void Daftar(){
        if (jenis_kelamin.getCheckedRadioButtonId()!= 0 ) {
            if (jenis_kelamin.getCheckedRadioButtonId() == laki.getId()) {
                jk = "Laki-Laki";
            } else if (jenis_kelamin.getCheckedRadioButtonId() == perempuan.getId()) {
                jk = "Perempuan";
            }
        }

        final HashMap<String,String> postData = new HashMap<>();
        postData.put("nama",nama.getText().toString());
        postData.put("email",email.getText().toString());
        postData.put("no_hp",no_hp.getText().toString());
        postData.put("jk",jk);
        postData.put("password",password.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Data Berhasil Di Kirim")){
                    Toast.makeText(getApplicationContext(),"Berhasil Mendaftar",Toast.LENGTH_SHORT).show();
                    KosongField();
                    finish();
                    Log.d(TAG,jk);
                }
            }
        });

        task.execute("https://batammall.co.id/ANDROID/daftar.php");
        task.setEachExceptionsHandler(new EachExceptionsHandler() {
            @Override
            public void handleIOException(IOException e) {
                Log.d(TAG,e.toString());
            }

            @Override
            public void handleMalformedURLException(MalformedURLException e) {
                Log.d(TAG,e.toString());
            }

            @Override
            public void handleProtocolException(ProtocolException e) {
                Log.d(TAG,e.toString());
            }

            @Override
            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                Log.d(TAG,e.toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
