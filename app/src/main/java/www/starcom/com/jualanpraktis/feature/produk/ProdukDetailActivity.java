package www.starcom.com.jualanpraktis.feature.produk;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.IDTansaksi.Shared;
import www.starcom.com.jualanpraktis.IDTansaksi.idtransaksi;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.Spinner.Variasi;
import www.starcom.com.jualanpraktis.adapter.ProdukGambarAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukSejenisAdapter;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.login;
import www.starcom.com.jualanpraktis.model.ListProduk;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class ProdukDetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    public static final String EXTRA_ID = "id_produk";
    public static final String EXTRA_NAMA = "nama_produk";
    public static final String EXTRA_HARGA = "harga_jual";
    public static final String EXTRA_BERAT = "berat";
    public static final String EXTRA_KET = "keterangan_produk";
    public static final String EXTRA_GAMBAR = "image_o";
    public static final String EXTRA_STOK = "stok";
    public static final String EXTRA_HARGA_ASLI = "harga_asli";
    public static final String UrlImage = "https://jualanpraktis.net/img/";

    idtransaksi idtransaksi ;
    loginuser user ;

    private ImageView gambar,main_gambar_list ;
    private TextView nama,harga,ket,harga_asli,txt_stok,txt_diskon ;
    CollapsingToolbarLayout collapsingToolbarLayout ;
    ElegantNumberButton numberButton;
    Button btn_keranjang ;

    String extra_harga,stok,diskon;
    String urlbase_api = "https://jualanpraktis.net/android/";
    private RecyclerView recyclerViewImage,rv_produk_sejenis;
    private Spinner spn_variasi;
    List<Variasi> variasiList = new ArrayList<>();
    String id_variasi,nama_variasi,stok_variasi,id_sub_kategori_produk;
    LinearLayout gambar_main_ll;
    CardView cvVariasi;
    ShimmerFrameLayout shimmerProdukLainnya;

    RadioGroup radio_group_jenisbelanja;
    String valueJenisBelanja,kode,id_member;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_detail);
        user = SharedPrefManager.getInstance(ProdukDetailActivity.this).getUser();
        AndroidNetworking.initialize(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gambar = findViewById(R.id.gambar_detail);
        final Uri uri = Uri.parse(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR));

        nama = findViewById(R.id.nama_produk);
        harga = findViewById(R.id.harga_produk);
        ket = findViewById(R.id.keterangan);
        harga_asli = findViewById(R.id.harga_asli);
        txt_stok = findViewById(R.id.stok);
        txt_diskon = findViewById(R.id.txt_diskon);
        numberButton = findViewById(R.id.number_button);
        recyclerViewImage = findViewById(R.id.recyclerViewImage);
        rv_produk_sejenis = findViewById(R.id.rv_produk_sejenis);
        shimmerProdukLainnya = findViewById(R.id.shimmerProdukLainnya);
        main_gambar_list = findViewById(R.id.main_gambar_list);
        gambar_main_ll = findViewById(R.id.gambar_main_ll);
        spn_variasi = findViewById(R.id.spn_variasi);
        cvVariasi = findViewById(R.id.cvVariasi);
         radio_group_jenisbelanja = findViewById(R.id.radio_group_jenisbelanja);
        btn_keranjang = findViewById(R.id.btn_keranjang);
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(EXTRA_GAMBAR)) {


                Glide.with(getApplicationContext())
                        .load(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR))
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false))
                        .into(gambar);
                Glide.with(getApplicationContext())
                        .load(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR))
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false))
                        .into(main_gambar_list);
                main_gambar_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Glide.with(getApplicationContext())
                                .load(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR))
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false))
                                .into(gambar);
                    }
                });

//                Log.d(TAG, uri.toString());


            }
            if (getIntent().getExtras().containsKey(EXTRA_KET)) {
                ket.setText(Html.fromHtml(getIntent().getExtras().getString(EXTRA_KET)));
            }

            if (getIntent().getExtras().containsKey(EXTRA_NAMA)) {

                nama.setText(getIntent().getExtras().getString(EXTRA_NAMA));
            }

            if (getIntent().getExtras().containsKey("diskon")) {
                diskon = getIntent().getExtras().getString("diskon");
                if (diskon.equals("0")){
                    txt_diskon.setVisibility(View.GONE);
                    harga_asli.setVisibility(View.GONE);
                }
                txt_diskon.setText("Hemat "+diskon+"%");
            }
            if (getIntent().getExtras().containsKey(EXTRA_HARGA_ASLI)) {

                 harga_asli.setText(FormatText.rupiahFormat(Double.parseDouble(getIntent().getExtras().getString(EXTRA_HARGA_ASLI))));
                 harga_asli.setPaintFlags(harga_asli.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (getIntent().getExtras().containsKey(EXTRA_HARGA)) {
             //   extra_harga = getIntent().getExtras().getString(EXTRA_HARGA);
             //   String hargadiskon = getIntent().getExtras().getString(EXTRA_HARGA);
             //   int harga_disc = Integer.parseInt(hargadiskon)*Integer.parseInt(diskon)/100;
                extra_harga = getIntent().getExtras().getString(EXTRA_HARGA);
                harga.setText(FormatText.rupiahFormat(Double.parseDouble(extra_harga)));
            }

            if (getIntent().getExtras().containsKey(EXTRA_STOK)) {
                stok = getIntent().getExtras().getString(EXTRA_STOK);

                //if (stok==null||stok.equals("")||stok.equals("null")){
                  //  stok = "0";
                  //  getDataVariasi();
              //  }


            }

            id_sub_kategori_produk = getIntent().getExtras().getString("id_sub_kategori_produk");
            kode = getIntent().getExtras().getString("kode");
            id_member = getIntent().getExtras().getString("id_member");

        }

        if (Shared.getInstance(this).isIdIn()) {

        }else {
            idTransaksi();
        }



        btn_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(ProdukDetailActivity.this).isLoggedIn()){
                    if (stok.equals("0")){
                        Toast.makeText(ProdukDetailActivity.this,"Stok barang kosong",Toast.LENGTH_SHORT).show();
                    }else if (Integer.parseInt(stok)<Integer.parseInt(numberButton.getNumber())){
                        Toast.makeText(ProdukDetailActivity.this,"Pemesanan melebihi dari stok yang ada",Toast.LENGTH_SHORT).show();
                    } else {
                        //validasiRadio();
                        valueJenisBelanja ="0";
                        if (valueJenisBelanja==null){
                            Toast.makeText(ProdukDetailActivity.this,"Pilih Jenis Belanja",Toast.LENGTH_SHORT).show();
                        }else {
                            pesan();
                        }

                    }

                }else{
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    Toast.makeText(ProdukDetailActivity.this,"Harap masuk terlebih dahulu",Toast.LENGTH_SHORT).show();
                }

            }
        });
        collapsingToolbarLayout = findViewById(R.id.collap);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarLayoutExpandedTextStyle);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        recyclerViewImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
      //  rv_produk_sejenis.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
      //  rv_produk_sejenis.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        rv_produk_sejenis.setLayoutManager(  new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.HORIZONTAL, false));
        spn_variasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Variasi variasi = variasiList.get(spn_variasi.getSelectedItemPosition());
                id_variasi = variasi.getId();
                nama_variasi = variasi.getVariasi();
                stok = variasi.getStok();

                txt_stok.setText(stok);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getDataGambar();
        getDataVariasi();
        getDataProdukSejenis();


    }

//    private void validasiRadio(){
//        int selectedId = radio_group_jenisbelanja.getCheckedRadioButtonId();
//        RadioButton radioButton = (RadioButton) findViewById(selectedId);
//
//        if (radioButton!=null) {
//            if (radioButton.getText().toString().equals("Belanja Biasa")) {
//                valueJenisBelanja = "0";
//            } else {
//                valueJenisBelanja = "1";
//            }
//        }
//    }

    private void getDataGambar(){
        AndroidNetworking.post(urlbase_api+"detail_gambar.php")
                .addBodyParameter("kode", kode)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");

                            ArrayList<HashMap<String,String>> hashMaps = new ArrayList<>();
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String,String> item = new HashMap<>();
                                item.put("gambar",object.getString("image"));
                                hashMaps.add(item);
                            }

                            ProdukGambarAdapter adapter = new ProdukGambarAdapter(ProdukDetailActivity.this,hashMaps,gambar);
                            recyclerViewImage.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void getDataVariasi(){
        AndroidNetworking.post(urlbase_api+"detail_variasi.php")
                .addBodyParameter("kode", kode)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");

                            if (array.length()!=0){
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);
                                    Variasi item = new Variasi();
                                    item.setId(object.getString("id"));
                                    item.setVariasi(object.getString("variasi"));
                                    item.setStok(object.getString("stok"));
                                    variasiList.add(item);
                                }
                                ArrayAdapter<Variasi> adapter = new ArrayAdapter<>(ProdukDetailActivity.this, R.layout.simple_spinner_text, variasiList);
                                adapter.setDropDownViewResource(R.layout.simple_spinner_text);
                                //  spinner.setPrompt("Jenis Perangkat : ");
                                spn_variasi.setAdapter(adapter);

                                if ( variasiList.get(0).getVariasi()==null || variasiList.get(0).getVariasi().equals("null")|| variasiList.get(0).getVariasi().equals("")){
                                    cvVariasi.setVisibility(View.GONE);
                                }else {
                                    cvVariasi.setVisibility(View.VISIBLE);
                                }

                                if (cvVariasi.getVisibility()==View.GONE){
                                    id_variasi = variasiList.get(0).getId();
                                    nama_variasi = "";
                                    stok = variasiList.get(0).getStok();

                                    txt_stok.setText(stok);
                                }
                            }else{
                                txt_stok.setText("0");
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    private void getDataProdukSejenis(){
        rv_produk_sejenis.setVisibility(View.GONE);
        shimmerProdukLainnya.setVisibility(View.VISIBLE);
        shimmerProdukLainnya.startShimmerAnimation();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        final String url = "https://jualanpraktis.net/android/produk_sejenis.php";
        AndroidNetworking.post(url)
                .addBodyParameter("id_sub_kategori_produk",id_sub_kategori_produk)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(ListProduk.ObjectSub.class, new ParsedRequestListener<ListProduk.ObjectSub>() {
                    @Override
                    public void onResponse(ListProduk.ObjectSub response) {
                        shimmerProdukLainnya.stopShimmerAnimation();
                        shimmerProdukLainnya.setVisibility(View.GONE);
                        rv_produk_sejenis.setVisibility(View.VISIBLE);
                        ProdukSejenisAdapter adapter = new ProdukSejenisAdapter(getApplicationContext(), response.produksejenislist);
                        rv_produk_sejenis.setAdapter(adapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerProdukLainnya.stopShimmerAnimation();
                        shimmerProdukLainnya.setVisibility(View.GONE);
                        rv_produk_sejenis.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void idTransaksi(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://jualanpraktis.net/android/id_transaksi.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    idtransaksi id = new idtransaksi(
                            response
                    );
                    Shared.getInstance(getApplicationContext()).idT(id);
                    Log.d(TAG,response);

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
    public void pesan(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://jualanpraktis.net/android/pesan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Data Berhasil Di Kirim")){
                 /**   int total_berat = Integer.parseInt(numberButton.getNumber())*Integer.parseInt(getIntent().getExtras().getString(EXTRA_BERAT));
                    new Database(getBaseContext()).addToChart(new order(
                            "",
                            getIntent().getExtras().getString(EXTRA_ID),
                            getIntent().getExtras().getString(EXTRA_NAMA)+" - "+nama_variasi,
                            extra_harga,
                            numberButton.getNumber(),
                            String.valueOf(total_berat)
                    ));
                    Log.d(TAG,getIntent().getExtras().getString(EXTRA_BERAT)); **/

                   // finish();
                   // Intent intent = new Intent(ProdukDetailActivity.this, MainActivity.class);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   // startActivity(intent);
                    ProdukDetailActivity.this.finish();
                    Toast.makeText(ProdukDetailActivity.this, "Berhasil Dimasukkan Kedalam Keranjang ", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("test Error", response);
                    Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+2);
                Date tomorrow = calendar.getTime();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String tomorrowAsString = dateFormat.format(tomorrow);
                String tomorrowAsString2 = timeFormat.format(tomorrow);
                int total_berat = Integer.parseInt(numberButton.getNumber())*Integer.parseInt(getIntent().getExtras().getString(EXTRA_BERAT));
                Map<String,String> params = new HashMap<>();

                params.put("id_produk",getIntent().getExtras().getString(EXTRA_ID));
                params.put("customer",user.getId());
                params.put("id_member",id_member);
              //  params.put("tgl_transaksi",tomorrowAsString);
             //   params.put("id_transaksi",idtransaksi.getId_transaksi());
               // params.put("berat_barang",getIntent().getExtras().getString(EXTRA_BERAT));
                params.put("berat",String.valueOf(total_berat));
                params.put("jumlah",numberButton.getNumber());
                params.put("harga_jual_item",extra_harga);
             //   params.put("harga_jual",extra_harga);
             //   params.put("time_limit",tomorrowAsString2);
                params.put("ket1",id_variasi);
            //  params.put("ket2",nama_variasi);
             //   params.put("bbl",valueJenisBelanja);

                Log.d("CobaKirim", params.toString());

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = SharedPrefManager.getInstance(ProdukDetailActivity.this).getUser();
    }
}
