package www.starcom.com.jualanpraktis;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import www.starcom.com.jualanpraktis.SubKategori.adaptersub;
import www.starcom.com.jualanpraktis.SubKategori.objectsub;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by ADMIN on 20/02/2018.
 */

public class SearchResultsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private objectsub.ObjectSub objectSub;
    private adaptersub adaptersub ;
    private RecyclerView recyclerView ;
    private Context context;
    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar ;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = new ProgressBar(this);
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.GONE);

        context = getApplicationContext();

        recyclerView = findViewById(R.id.rv_item);
        recyclerView.setHasFixedSize(true);


        gridLayoutManager = new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(gridLayoutManager);


        searchView = findViewById(R.id.cari);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Menampilkan Data dari Database
    public void GetData(String produk) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, "https://batammall.co.id/ANDROID/cari.php?nama_produk="+produk,
                        new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
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
    public boolean onQueryTextSubmit(String query) {
        GetData(query);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        recyclerView.setAdapter(null);
        return true;
    }
}
