package www.starcom.com.jualanpraktis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.starcom.com.jualanpraktis.Spinner.Kecamatan;
import www.starcom.com.jualanpraktis.Spinner.KotaKabupaten;
import www.starcom.com.jualanpraktis.Spinner.Provinsi;
import www.starcom.com.jualanpraktis.databinding.ActivityResultTransferBinding;

import static www.starcom.com.jualanpraktis.alamat_pengiriman.BASE_URL;
import static www.starcom.com.jualanpraktis.alamat_pengiriman.KEY;

/**
 * Created by ADMIN on 12/02/2018.
 */

public class daftar extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();

    Activity activity = daftar.this;
    //ActivityResultTransferBinding binding;


    private EditText nama,email,no_hp,password,ulangpass,input_alamat,nama_toko, no_ktp, no_npwp, atas_nama, no_rek, nama_bank;
    //private RadioGroup jenis_kelamin;
    //private RadioButton laki,perempuan;
    private Button btn_daftar ;
    private String mediaPath;
    private ImageView uploadPicture;

    String nama_kota,nama_wilayah,nama_provinsi ;
    Spinner spinner1,spinner2,spin_provinsi ;
    List<KotaKabupaten> kotaKabupatenList = new ArrayList<>();
    List<Kecamatan> kecamatanList = new ArrayList<>();
    List<Provinsi> provinsiList = new ArrayList<>();
    ProgressDialog progressDialog;

    //String jk = "" ;
    String id_kota,id_wilayah,id_provinsi ;
    String url = "https://jualanpraktis.net/android/signup.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        progressDialog  = new ProgressDialog(daftar.this);
        AndroidNetworking.initialize(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        nama = findViewById(R.id.input_nama);
        email = findViewById(R.id.input_email);
        no_hp = findViewById(R.id.input_nohp);
        password = findViewById(R.id.input_pass);
        ulangpass = findViewById(R.id.input_ulang);
        spinner1 = findViewById(R.id.spin_kota);
        spinner2 = findViewById(R.id.spin_kecamatan);
        spin_provinsi = findViewById(R.id.spin_provinsi);
        input_alamat = findViewById(R.id.input_alamat);
        nama_toko = findViewById(R.id.input_nama_toko);
        no_ktp = findViewById(R.id.input_no_ktp);
        no_npwp = findViewById(R.id.input_no_npwp);
        atas_nama = findViewById(R.id.input_atas_nama);
        no_rek = findViewById(R.id.input_no_rek);
        nama_bank = findViewById(R.id.input_nama_bank);
        uploadPicture = findViewById(R.id.upload_profile_picture);
        btn_daftar = findViewById(R.id.btn_daftar);


//        jenis_kelamin = findViewById(R.id.RG);
//        laki = findViewById(R.id.laki);
//        perempuan = findViewById(R.id.perempuan);

//        UlangPass();
        btn_daftar.setOnClickListener(this);
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(activity)
                        .crop(340,340)
                        .compress(512)
                        .start();
            }
        });
       getProvinsi();

        spin_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Provinsi provinsi = provinsiList.get(spin_provinsi.getSelectedItemPosition());
                id_provinsi = provinsi.getProvince_id();
                nama_provinsi = provinsi.getProvince();
                if (id_provinsi != null) {
                    // spinnerData2(id_kota);
                    getKotaKabupaten(id_provinsi);
                    Log.d(TAG, id_provinsi);
                }else {
                    Log.d(TAG,""+ id_provinsi);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //   id_kota = ((SObject1.Object1.Results) spinner1.getSelectedItem()).idKota;
                KotaKabupaten kotaKabupaten = kotaKabupatenList.get(spinner1.getSelectedItemPosition());
                id_kota = kotaKabupaten.getCity_id();
                nama_kota = kotaKabupaten.getCity_name();
                if (id_kota != null) {
                    // spinnerData2(id_kota);
                    getKecamatan(id_kota);
                    Log.d(TAG, id_kota);
                }else {
                    Log.d(TAG,""+ id_kota);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, id_kota);
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Kecamatan kecamatan = kecamatanList.get(spinner2.getSelectedItemPosition());
                id_wilayah = kecamatan.getSubdistrict_id();
                nama_wilayah = kecamatan.getSubdistrict_name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        }else if (input_alamat.getText().toString().equals("")){
            input_alamat.setError("Alamat belum di isi");
            input_alamat.requestFocus();
        } else if (password.getText().toString().equals("")) {
            password.setError("Password belum di isi");
            password.requestFocus();
        }
        Daftar();
//        } else if (ulangpass.getText().toString().equals("")) {
//            ulangpass.setError("Password belum di isi");
//        } else if (!ulangpass.getText().toString().equals(password.getText().toString())) {
//            ulangpass.setError("Password salah");

//        } else if (jenis_kelamin.getCheckedRadioButtonId() == -1) {
//            Toast.makeText(this, "Anda Belum Memilih Cara Memperoleh Informasi", Toast.LENGTH_LONG).show();
//        }  else
        //Daftar
//        if (v == btn_daftar) {
//            if(mediaPath != null) {
//                Daftar();
//            }else{
//                Toast.makeText(activity,"Foto belum di upload",Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            Uri uri = data.getData();
            uploadPicture.setImageURI(uri);
            mediaPath = uri.getPath();
        }
    }

    public void KosongField(){
        nama.setText("");
        email.setText("");
        no_hp.setText("");
        password.setText("");
        no_ktp.setText("");
        no_npwp.setText("");
        atas_nama.setText("");
        no_rek.setText("");
        nama_bank.setText("");
        //ulangpass.setText("");
        //jenis_kelamin.check(0);

    }

//    public void UlangPass(){
//
//        ulangpass.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String Password = password.getText().toString();
//                if (!ulangpass.getText().toString().equals(Password)){
//                    ulangpass.setError("Password Salah");
//                }
//            }
//        });
//    }

    public void Daftar(){
//        if (jenis_kelamin.getCheckedRadioButtonId()!= 0 ) {
//            if (jenis_kelamin.getCheckedRadioButtonId() == laki.getId()) {
//                jk = "Laki-Laki";
//            } else if (jenis_kelamin.getCheckedRadioButtonId() == perempuan.getId()) {
//                jk = "Perempuan";
//            }
//        }
        progressDialog.setTitle("Daftar");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final HashMap<String,String> postData = new HashMap<>();
        postData.put("nama",nama.getText().toString());
        postData.put("nama_toko",nama_toko.getText().toString());
        postData.put("provinsi",nama_provinsi);
        postData.put("kota",nama_kota);
        postData.put("kecamatan",nama_wilayah);
        postData.put("alamat",input_alamat.getText().toString());
        postData.put("no_ktp",no_ktp.getText().toString());
        postData.put("no_npwp",no_npwp.getText().toString());
        postData.put("no_hp",no_hp.getText().toString());
        postData.put("email",email.getText().toString());
        postData.put("password",password.getText().toString());
        postData.put("atas_nama",atas_nama.getText().toString());
        postData.put("no_rek",no_rek.getText().toString());
        postData.put("nama_bank", nama_bank.getText().toString());
//        postData.put("nama_provinsi",nama_provinsi);
//        postData.put("nama_kota",nama_kota);
//        postData.put("nama_kecamatan",nama_wilayah);

//        postData.put("provinsi",id_provinsi);
//        postData.put("kota",id_kota);
//        postData.put("kecamatan",id_wilayah);
        //postData.put("files", body.toString());

        AndroidNetworking.upload("https://jualanpraktis.net/android/signup.php")
                .addMultipartParameter(postData)
                .addMultipartFile("files",new File(mediaPath))
                .setTag(activity)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("Pendaftaran berhasil")){
                            // progressDialog.dismiss();
//                            Intent intent = new Intent(activity, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
                            Toast.makeText(getApplicationContext(),"We've send a message to the email provided with a link to active your account. This helps us verify your identity.",Toast.LENGTH_LONG).show();
                            new AlertDialog.Builder(daftar.this)
                            .setTitle("Check Your Email")
                            .setMessage("We've send a message to the email provided with a link to active your account. This helps us verify your identity.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    KosongField();
                                    finish();
                                }
                            }).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(activity,"Put error here",Toast.LENGTH_LONG).show();
                    }
                });


//        postData.put("jk",jk);
//        postData.put("provinsi",id_provinsi);
//        postData.put("kota",id_kota);
//        postData.put("kecamatan",id_wilayah);



//        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
//            @Override
//            public void processFinish(String s) {
//                progressDialog.dismiss();
//                if (s.contains("Pendaftaran berhasil")){
//                    Toast.makeText(getApplicationContext(),"We've send a message to the email provided with a link to active your account. This helps us verify your identity.",Toast.LENGTH_LONG).show();
//                    new AlertDialog.Builder(daftar.this)
//                            .setTitle("Check Your Email")
//                            .setMessage("We've send a message to the email provided with a link to active your account. This helps us verify your identity.")
//                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    KosongField();
//                                    finish();
//                                }
//                            }).show();
//
////            Log.d(TAG,body.toString());
//                }else{
//                    Toast.makeText(getApplicationContext(),"Gagal Mendaftar",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        task.execute("https://trading.my.id/android/signup.php");
//        task.setEachExceptionsHandler(new EachExceptionsHandler() {
//            @Override
//            public void handleIOException(IOException e) {
//                Log.d(TAG,e.toString());
//            }
//
//            @Override
//            public void handleMalformedURLException(MalformedURLException e) {
//                Log.d(TAG,e.toString());
//            }
//
//            @Override
//            public void handleProtocolException(ProtocolException e) {
//                Log.d(TAG,e.toString());
//            }
//
//            @Override
//            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
//                Log.d(TAG,e.toString());
//            }
//        });

    }

        private void getProvinsi(){
        AndroidNetworking.get(BASE_URL + "province")
                .addHeaders("key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        provinsiList.clear();
                        try {
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                Provinsi item = new Provinsi();
                                item.setProvince_id(obj.getString("province_id"));
                                item.setProvince(obj.getString("province"));
                                provinsiList.add(item);
                            }


                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
                            spin_provinsi.setVisibility(View.VISIBLE);
                            ArrayAdapter<Provinsi> adapter = new ArrayAdapter<>(daftar.this, R.layout.support_simple_spinner_dropdown_item, provinsiList);
                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            //  spinner.setPrompt("Jenis Perangkat : ");
                            spin_provinsi.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
    private void getKotaKabupaten(String id_provinsi){
        AndroidNetworking.get(BASE_URL + "city?province="+id_provinsi)
                .addHeaders("key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        kotaKabupatenList.clear();
                        try {
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                KotaKabupaten item = new KotaKabupaten();
                                item.setCity_id(obj.getString("city_id"));
                                item.setCity_name(obj.getString("city_name"));
                                kotaKabupatenList.add(item);
                            }


                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
                            spinner1.setVisibility(View.VISIBLE);
                            ArrayAdapter<KotaKabupaten> adapter = new ArrayAdapter<KotaKabupaten>(daftar.this, R.layout.support_simple_spinner_dropdown_item, kotaKabupatenList);
                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            //  spinner.setPrompt("Jenis Perangkat : ");
                            spinner1.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
    private void getKecamatan(String idkota){
        AndroidNetworking.get(BASE_URL + "subdistrict?city="+idkota)
                .addHeaders("key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        kecamatanList.clear();
                        try {
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                Kecamatan item = new Kecamatan();
                                item.setSubdistrict_id(obj.getString("subdistrict_id"));
                                item.setSubdistrict_name(obj.getString("subdistrict_name"));
                                kecamatanList.add(item);
                            }


                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
                            spinner2.setVisibility(View.VISIBLE);
                            ArrayAdapter<Kecamatan> adapter = new ArrayAdapter<Kecamatan>(daftar.this, R.layout.support_simple_spinner_dropdown_item, kecamatanList);
                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            //  spinner.setPrompt("Jenis Perangkat : ");
                            spinner2.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
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
