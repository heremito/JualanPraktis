package www.starcom.com.jualanpraktis.SubKategori;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.IDTansaksi.Shared;
import www.starcom.com.jualanpraktis.IDTansaksi.idtransaksi;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.R;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class produk_detail extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    public static final String EXTRA_ID = "id_produk";
    public static final String EXTRA_NAMA = "nama_produk";
    public static final String EXTRA_HARGA = "harga_jual";
    public static final String EXTRA_BERAT = "berat";
    public static final String EXTRA_KET = "keterangan_produk";
    public static final String EXTRA_GAMBAR = "image_o";
    public static final String UrlImage = "https://batammall.co.id/img/";

    idtransaksi idtransaksi ;

    private ImageView gambar ;
    private TextView nama,harga,ket ;
    CollapsingToolbarLayout collapsingToolbarLayout ;
    ElegantNumberButton numberButton;
    Button btn_keranjang ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gambar = findViewById(R.id.gambar_detail);
        final Uri uri = Uri.parse(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR));

        nama = findViewById(R.id.nama_produk);
        harga = findViewById(R.id.harga_produk);
        ket = findViewById(R.id.keterangan);
        numberButton = findViewById(R.id.number_button);
        btn_keranjang = findViewById(R.id.btn_keranjang);
        btn_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pesan();
            }
        });
        collapsingToolbarLayout = findViewById(R.id.collap);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(EXTRA_GAMBAR)) {
                Glide.with(this).load(uri).into(gambar);
            }
            if (getIntent().getExtras().containsKey(EXTRA_KET)) {
                nama.setText(getIntent().getExtras().getString(EXTRA_KET));
            }
            if (getIntent().getExtras().containsKey(EXTRA_HARGA)) {
                harga.setText(getIntent().getExtras().getString(EXTRA_HARGA));
            }
            if (getIntent().getExtras().containsKey(EXTRA_NAMA)) {
                ket.setText(getIntent().getExtras().getString(EXTRA_NAMA));
            }

        }

        if (Shared.getInstance(this).isIdIn()) {

        }else {
            idTransaksi();
        }

    }

    private void idTransaksi(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://batammall.co.id/ANDROID/id_transaksi.php", new Response.Listener<String>() {
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
        StringRequest request = new StringRequest(Request.Method.POST, "https://batammall.co.id/ANDROID/pesan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Data Berhasil Di Kirim")){
                    new Database(getBaseContext()).addToChart(new order(
                            "",
                            getIntent().getExtras().getString(EXTRA_ID),
                            getIntent().getExtras().getString(EXTRA_NAMA),
                            getIntent().getExtras().getString(EXTRA_HARGA),
                            numberButton.getNumber(),
                            getIntent().getExtras().getString(EXTRA_BERAT)
                    ));
                    Log.d(TAG,getIntent().getExtras().getString(EXTRA_BERAT));

                    finish();
                    Toast.makeText(produk_detail.this, "Berhasil Dimasukkan Kedalam Keranjang ", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String tomorrowAsString = dateFormat.format(tomorrow);
                String tomorrowAsString2 = timeFormat.format(tomorrow);
                Map<String,String> params = new HashMap<>();
                params.put("id_transaksi",idtransaksi.getId_transaksi());
                params.put("tgl_transaksi",tomorrowAsString);
                params.put("id_produk",getIntent().getExtras().getString(EXTRA_ID));
                params.put("berat",getIntent().getExtras().getString(EXTRA_BERAT));
                params.put("jumlah",numberButton.getNumber());
                params.put("harga_jual",getIntent().getExtras().getString(EXTRA_HARGA));
                params.put("time_limit",tomorrowAsString2);
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
}
