package www.starcom.com.jualanpraktis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.Spinner.Kecamatan;
import www.starcom.com.jualanpraktis.Spinner.KotaKabupaten;
import www.starcom.com.jualanpraktis.Spinner.Layanan;
import www.starcom.com.jualanpraktis.Spinner.Provinsi;
import www.starcom.com.jualanpraktis.Spinner.SAdapter1;
import www.starcom.com.jualanpraktis.Spinner.SAdapter2;
import www.starcom.com.jualanpraktis.Spinner.SObject1;
import www.starcom.com.jualanpraktis.Spinner.SObject2;
import www.starcom.com.jualanpraktis.feature.pembayaran.PembayaranActivity;

/**
 * Created by ADMIN on 14/02/2018.
 */

public class alamat_pengiriman extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();

    public static final String EXTRA_TOTAL = "total";
    public static final String EXTRA_BERAT = "berat";
    public static final String KEY = "9bc530edfde2ffdba603d779832349cf";
    public static final String BASE_URL = "https://pro.rajaongkir.com/api/";
    String id_kota,id_wilayah,harga,expedisi,kodeKurir,id_provinsi ;
    EditText nama,alamat,nohp,edt_total_berat ;
    TextView id_transaksi,ongkir ;
    Spinner spinner1,spinner2,spin_kurir,spin_layanan,spin_provinsi ;
    Button btnKirim;
    private SObject1.Object1 listData1;
    private SAdapter1 spinnerAdapter1;
    private SObject2.Object2 listData2;
    private SAdapter2 spinnerAdapter2;
    private boolean mSpinnerInitialized;
    loginuser user ;
 //   www.starcom.com.batammall.IDTansaksi.idtransaksi idtransaksi ;

    ProgressDialog progressDialog;
    List<KotaKabupaten> kotaKabupatenList = new ArrayList<>();
    List<Kecamatan> kecamatanList = new ArrayList<>();
    List<Provinsi> provinsiList = new ArrayList<>();
    List<Layanan> layananList = new ArrayList<>();
   // List<HashMap<String,String>> kurirList = new ArrayList<>();
    String total_berat,onngkir_ro;
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();
    String idTransaksi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pengiriman);

        AndroidNetworking.initialize(getApplicationContext());
        progressDialog = new ProgressDialog(alamat_pengiriman.this);
        user = SharedPrefManager.getInstance(this).getUser();
     //   idtransaksi = Shared.getInstance(this).getIdT();

        id_transaksi = findViewById(R.id.id_transaksi);
        nama = findViewById(R.id.nama);
        alamat= findViewById(R.id.alamat);
        nohp = findViewById(R.id.nohp);
        ongkir = findViewById(R.id.biaya);
        spinner1 = findViewById(R.id.spin_kota);
        spinner2 = findViewById(R.id.spin_kecamatan);
        spin_kurir = findViewById(R.id.spin_kurir);
        spin_layanan = findViewById(R.id.spin_layanan);
        spin_provinsi = findViewById(R.id.spin_provinsi);
        edt_total_berat = findViewById(R.id.edt_total_berat);
        btnKirim = findViewById(R.id.button);
        btnKirim.setOnClickListener(this);
        String tes = getIntent().getExtras().getString(EXTRA_TOTAL);
        total_berat = getIntent().getExtras().getString(EXTRA_BERAT);
        idTransaksi = getIntent().getExtras().getString("id_transaksi");
        dataList = (ArrayList<HashMap<String, String>>) getIntent().getExtras().getSerializable("dataList");

        Log.d(TAG,tes);

        edt_total_berat.setText(total_berat);
        spin_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Provinsi provinsi = provinsiList.get(spin_provinsi.getSelectedItemPosition());
                id_provinsi = provinsi.getProvince_id();
                if (id_provinsi != null) {
                    // spinnerData2(id_kota);
                    KotaKabupaten(id_provinsi);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Kecamatan kecamatan = kecamatanList.get(spinner2.getSelectedItemPosition());
                id_wilayah = kecamatan.getSubdistrict_id();
                if (id_wilayah != null) {
                   // ongkir(id_wilayah);
                    ArrayAdapter<String> adapterBulan = new ArrayAdapter<String>(alamat_pengiriman.this, R.layout.support_simple_spinner_dropdown_item, dataKurir);
                    adapterBulan.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spin_kurir.setAdapter(adapterBulan);
                    Log.d(TAG, id_wilayah);
                }else {
                    Log.d(TAG,""+ id_wilayah);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_kurir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spin_kurir.getSelectedItemPosition()==0){
                    kodeKurir = "jne";
                }else if (spin_kurir.getSelectedItemPosition()==1){
                    kodeKurir = "jnt";
                }else if (spin_kurir.getSelectedItemPosition()==2){
                    kodeKurir = "sicepat";
                }else if (spin_kurir.getSelectedItemPosition()==3){
                    kodeKurir = "tiki";
                }

                //48 itu id kota batam
                cost("48",id_wilayah,edt_total_berat.getText().toString(),kodeKurir);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spin_layanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Layanan layanan = layananList.get(spin_layanan.getSelectedItemPosition());
                onngkir_ro = String.valueOf(layanan.getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        id_transaksi.setText(idTransaksi);
        //spinnerData1();
        getProvinsi();
        //setting the values to the textviews
        nama.setText(user.getNama());
    }

    private void spinnerData1(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://jualanpraktis.net/android/pilih_kota.php", new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://jualanpraktis.net/android/pilih_wilayah.php?idKota="+id, new Response.Listener<String>() {
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
            "https://jualanpraktis.net/android/ongkir.php?idKecamatan="+id, (JSONArray) null, new Response.Listener<JSONArray>() {
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

          //  finish();
            kirim();
        }

    }

    public void kirim(){
        progressDialog.setTitle("Memproses Pemesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "https://jualanpraktis.net/android/transaksi1.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response.equals("Data Berhasil Di Kirim")){
                    String Ongkir = onngkir_ro;
                    String to = getIntent().getExtras().getString(EXTRA_TOTAL);
                    int tot = 0;
                    if (to != null) {
                        tot = Integer.parseInt(to.replace(",",""));
                    }
                    int total = (tot)+(Integer.parseInt(Ongkir));
                    Intent intent = new Intent(alamat_pengiriman.this, PembayaranActivity.class);
                    intent.putExtra("id_transaksi",idTransaksi);
                    intent.putExtra("total",Integer.toString(total));
                    intent.putExtra("no_hp",nohp.getText().toString());
                    intent.putExtra("dataList",dataList);
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
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Gagal Melakukan Pemesanan", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();

                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String currentDateTime = currentDate +" "+ currentTime;

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String tomorrowAsString = dateFormat.format(tomorrow);
                String tomorrowAsString2 = timeFormat.format(tomorrow);

                Map<String,String> params = new HashMap<String,String>();
                String Ongkir = onngkir_ro;
                String to = getIntent().getExtras().getString(EXTRA_TOTAL);
                int tot = 0;
                if (to != null) {
                    tot = Integer.parseInt(to.replace(",",""));
                }
                int total = (tot)+(Integer.parseInt(Ongkir));
                params.put("id_customer",user.getId());
                params.put("id_transaksi",idTransaksi);
                params.put("tgl_transaksi",currentDateTime);
                params.put("nama_penerima",nama.getText().toString());
                params.put("alamat",alamat.getText().toString());

                params.put("city_destination",id_kota);
                params.put("province_destination",id_provinsi);
                params.put("subdistrict_destination",id_wilayah);
                params.put("no_hp",nohp.getText().toString());
                params.put("hp_pemesan",nohp.getText().toString());
                params.put("time_limit_order",tomorrowAsString);
                params.put("time_limit",tomorrowAsString2);

                params.put("total_belanja",Integer.toString(tot));
                params.put("weight",getIntent().getExtras().getString(EXTRA_BERAT));
                params.put("courier",kodeKurir);
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
                params.put("id_transaksi",idTransaksi);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
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
                            ArrayAdapter<Provinsi> adapter = new ArrayAdapter<>(alamat_pengiriman.this, R.layout.support_simple_spinner_dropdown_item, provinsiList);
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
    private void KotaKabupaten(String id_provinsi){
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
                            ArrayAdapter<KotaKabupaten> adapter = new ArrayAdapter<KotaKabupaten>(alamat_pengiriman.this, R.layout.support_simple_spinner_dropdown_item, kotaKabupatenList);
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
                            ArrayAdapter<Kecamatan> adapter = new ArrayAdapter<Kecamatan>(alamat_pengiriman.this, R.layout.support_simple_spinner_dropdown_item, kecamatanList);
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
    private void cost(String origin,String destination,String weight,String courier){

        HashMap<String,String> param = new HashMap<>();
        param.put("origin",origin);
        param.put("originType","city");
        param.put("destination",destination);
        param.put("destinationType","subdistrict");
        param.put("weight",weight);
        param.put("courier",courier);

        AndroidNetworking.post(BASE_URL+ "cost")
                .addHeaders("key",KEY)
                .addBodyParameter(param)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            layananList.clear();
                            //get main result
                            JSONObject object = response.getJSONObject("rajaongkir");
                            JSONArray array = object.getJSONArray("results");

                            //get list layanan
                            JSONObject object1 = array.getJSONObject(0);
                            JSONArray array1 = object1.getJSONArray("costs");
                            for (int i = 0; i < array1.length(); i++){
                                JSONObject obj = array1.getJSONObject(i);
                                Layanan item = new Layanan();
                                item.setService(obj.getString("service"));
                                item.setDescription(obj.getString("description"));

                                JSONArray arraycost = obj.getJSONArray("cost");
                                JSONObject objCost = arraycost.getJSONObject(0);

                                item.setValue(objCost.getInt("value"));
                                NumberFormat nf = new DecimalFormat("#,###");
                                item.setDecimalValue("Rp. "+ nf.format(objCost.getInt("value")));
                                item.setEtd(objCost.getString("etd"));
                                layananList.add(item);
                            }


                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
                          //  spinner2.setVisibility(View.VISIBLE);
                            ArrayAdapter<Layanan> adapter = new ArrayAdapter<Layanan>(alamat_pengiriman.this, R.layout.custom_spinner_dropdown_item, layananList);
                            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            //  spinner.setPrompt("Jenis Perangkat : ");
                            spin_layanan.setAdapter(adapter);

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
    private String[] dataKurir = new String[]{
            "JNE","JNT","SiCepat","Tiki"
    };



}
