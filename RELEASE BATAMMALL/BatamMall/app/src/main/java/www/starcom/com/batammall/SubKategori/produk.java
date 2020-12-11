package www.starcom.com.jualanpraktis.SubKategori;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.keranjang2;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class produk extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar ;
    private objectsub.ObjectSub objectSub;
    private adaptersub adaptersub ;
    private RecyclerView recyclerView ;
    private Context context;
    GridLayoutManager gridLayoutManager;
    FloatingActionButton keranjang ;
    ImageView jumlah ;

    List<order> list = new ArrayList<>();
    order order;
    int total = 0 ;


    public static final String EXTRA_ID = "id_sub_kategori_produk";
    public static final String EXTRA_SUB = "sub_kategori_produk";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getExtras().getString(EXTRA_SUB));

        keranjang= findViewById(R.id.fab_keranjang);
        keranjang.setOnClickListener(this);
        jumlah = findViewById(R.id.item_jumlah);

        context = getApplicationContext();

        recyclerView = findViewById(R.id.rv_produk);
        recyclerView.setHasFixedSize(true);

        //recyclerViewLayoutManager = new GridLayoutManager(context,2);
        //recyclerView.setLayoutManager(recyclerViewLayoutManager);

        gridLayoutManager = new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);


        GetData(urlsub.URL_SUB1(getIntent().getExtras().getString(EXTRA_ID)));


    }

    @Override
    protected void onResume() {
        super.onResume();
        jml(0);

    }



    public void jml(int Total){
        list = new Database(getApplicationContext()).getPesan();
        for (order order:list)

                Total += (Integer.parseInt(order.getJumlah()));
                total = Total;
                String tot = Integer.toString(total);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound("" + tot, Color.RED);
                jumlah.setImageDrawable(drawable);



    }

    // Menampilkan Data dari Database
    public void GetData(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectSub = mGson.fromJson(response, objectsub.ObjectSub.class);
                    adaptersub = new adaptersub(getApplicationContext(), objectSub.sub1_kategori1);
                    recyclerView.setAdapter(adaptersub);
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
    public void onClick(View v) {
        Intent intent = new Intent(produk.this,keranjang2.class);
        startActivity(intent);
    }
}
