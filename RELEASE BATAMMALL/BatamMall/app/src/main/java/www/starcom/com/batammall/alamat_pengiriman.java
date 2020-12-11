package www.starcom.com.jualanpraktis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import www.starcom.com.jualanpraktis.IDTansaksi.Shared;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.Spinner.SAdapter1;
import www.starcom.com.jualanpraktis.Spinner.SAdapter2;
import www.starcom.com.jualanpraktis.Spinner.SObject1;
import www.starcom.com.jualanpraktis.Spinner.SObject2;

/**
 * Created by ADMIN on 14/02/2018.
 */

public class alamat_pengiriman extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();

    public static final String EXTRA_TOTAL = "total";
    public static final String EXTRA_BERAT = "berat";

    String id_kota,id_wilayah,harga,expedisi ;
    EditText nama,alamat,nohp ;
    TextView id_transaksi,ongkir ;
    Spinner spinner1,spinner2 ;
    Button btnKirim;
    private SObject1.Object1 listData1;
    private SAdapter1 spinnerAdapter1;
    private SObject2.Object2 listData2;
    private SAdapter2 spinnerAdapter2;
    private boolean mSpinnerInitialized;
    loginuser user ;
    www.starcom.com.jualanpraktis.IDTansaksi.idtransaksi idtransaksi ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pengiriman);

        user = SharedPrefManager.getInstance(this).getUser();
        idtransaksi = Shared.getInstance(this).getIdT();

        id_transaksi = findViewById(R.id.id_transaksi);
        nama = findViewById(R.id.nama);
        alamat= findViewById(R.id.alamat);
        nohp = findViewById(R.id.nohp);
        ongkir = findViewById(R.id.biaya);
        spinner1 = findViewById(R.id.spin_kota);
        spinner2 = findViewById(R.id.spin_kecamatan);
        btnKirim = findViewById(R.id.button);
        btnKirim.setOnClickListener(this);
        String tes = getIntent().getExtras().getString(EXTRA_TOTAL);

        Log.d(TAG,tes);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kota = ((SObject1.Object1.Results) spinner1.getSelectedItem()).idKota;
                if (id_kota != null) {
                    spinnerData2(id_kota);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_wilayah = ((SObject2.Object2.Results) spinner2.getSelectedItem()).idKecamatan;
                if (id_wilayah != null) {
                    ongkir(id_wilayah);
                    Log.d(TAG, id_wilayah);
                }else {
                    Log.d(TAG,""+ id_wilayah);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        id_transaksi.setText(idtransaksi.getId_transaksi());
        spinnerData1();

        //setting the values to the textviews
        nama.setText(user.getNama());
    }

    private void spinnerData1(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://batammall.co.id/ANDROID/pilih_kota.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    listData1 = gson.fromJson(response, SObject1.Object1.class);
                    Log.d(TAG,response);
                    if (null != listData1) {
                        assert spinner1 != null;
                        spinner1.setVisibility(View.VISIBLE);
                        spinnerAdapter1 = new SAdapter1(alamat_pengiriman.this, listData1.object1);
                        spinner1.setAdapter(spinnerAdapter1);
                    }


                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Periksa Jaringan Internet Anda", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void spinnerData2(String id){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://batammall.co.id/ANDROID/pilih_wilayah.php?idKota="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    listData2 = gson.fromJson(response, SObject2.Object2.class);
                    Log.d(TAG,response);
                    if (null != listData2) {
                        assert spinner2 != null;
                        spinner2.setVisibility(View.VISIBLE);
                        spinnerAdapter2 = new SAdapter2(alamat_pengiriman.this, listData2.object2);
                        spinner2.setAdapter(spinnerAdapter2);
                    }

                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Periksa Jaringan Internet Anda", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void ongkir(String id){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
            "https://batammall.co.id/ANDROID/ongkir.php?idKecamatan="+id, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        NumberFormat nf = new DecimalFormat("#,###");
                        harga = jsonObject.getString("harga");
                        expedisi = jsonObject.getString("nama_expedisi");
                        ongkir.setText(expedisi+ " | Rp "+harga);


                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: ", e);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Periksa Jaringan Internet Anda", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View v) {

        if (nama.getText().toString().equals("")) {
            nama.setError("Nama belum di isi");
            nama.requestFocus();
        } else if (alamat.getText().toString().equals("")) {
            alamat.setError("Alamat belum di isi");
            alamat.requestFocus();
        } else if (nohp.getText().toString().equals("")) {
            nohp.setError("No Hp belum di isi");
            nohp.requestFocus();
        } else {
            kirim();
        }

    }

    public void kirim(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://batammall.co.id/ANDROID/transaksi1.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Data Berhasil Di Kirim")){
                    String Ongkir = harga;
                    String to = getIntent().getExtras().getString(EXTRA_TOTAL);
                    int tot = 0;
                    if (to != null) {
                        tot = Integer.parseInt(to.replace(".",""));
                    }
                    int total = (tot)+(Integer.parseInt(Ongkir));
                    Intent intent = new Intent(alamat_pengiriman.this,konfirmasi.class);
                    intent.putExtra("id_transaksi",idtransaksi.getId_transaksi());
                    intent.putExtra("total",Integer.toString(total));
                    startActivity(intent);
                    StatusTransaksi();
                    finish();
                    Toast.makeText(getApplicationContext(), "Berhasil Melakukan Pemesanan", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Gagal Melakukan Pemesanan", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String tomorrowAsString = dateFormat.format(tomorrow);
                String tomorrowAsString2 = timeFormat.format(tomorrow);

                Map<String,String> params = new HashMap<String,String>();
                String Ongkir = harga;
                String to = getIntent().getExtras().getString(EXTRA_TOTAL);
                int tot = 0;
                if (to != null) {
                    tot = Integer.parseInt(to.replace(".",""));
                }
                int total = (tot)+(Integer.parseInt(Ongkir));
                params.put("id_customer",Integer.toString(user.getId()));
                params.put("id_transaksi",idtransaksi.getId_transaksi());
                params.put("nama_penerima",nama.getText().toString());
                params.put("alamat",alamat.getText().toString());
                params.put("idKota",((SObject1.Object1.Results) spinner1.getSelectedItem()).idKota);
                params.put("idKecamatan",((SObject2.Object2.Results) spinner2.getSelectedItem()).idKecamatan);
                params.put("no_hp",nohp.getText().toString());
                params.put("time_limit_order",tomorrowAsString);
                params.put("time_limit",tomorrowAsString2);
                params.put("total_belanja",getIntent().getExtras().getString(EXTRA_TOTAL));
                params.put("total_berat",getIntent().getExtras().getString(EXTRA_BERAT));
                params.put("ongkos_kirim",Ongkir);
                params.put("tot_ongkos_kirim",Ongkir);
                params.put("total_bayar",Integer.toString(total));
                return params;

            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }

    public void StatusTransaksi(){
        StringRequest request = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id_transaksi",idtransaksi.getId_transaksi());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
