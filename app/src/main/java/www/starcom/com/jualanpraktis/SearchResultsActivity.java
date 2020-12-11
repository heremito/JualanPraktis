package www.starcom.com.jualanpraktis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;
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

    private SearchView.SearchAutoComplete searchAutoComplete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AndroidNetworking.initialize(getApplicationContext());

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
         searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);




        /**  String data[]={"Emmanuel", "Olayemi", "Henrry", "Mark", "Steve", "Ayomide", "David", "Anthony", "Adekola", "Adenuga"};
          SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
          ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, data);
          searchAutoComplete.setAdapter(dataAdapter); **/

      getAllProduk();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /** getAllProdukName() gak dipake*/
    private void getAllProdukName(){
      //  progressBar.setVisibility(View.VISIBLE);
        String url = "https://trading.my.id/android/produk.php";
        AndroidNetworking.get(url)
                .setTag(SearchResultsActivity.this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("sub1_kategori1");
                            final String searchItems[]= new String[array.length()];
                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                searchItems[i] = obj.getString("nama_produk");
                            }
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchResultsActivity.this, android.R.layout.simple_list_item_1, searchItems);
                            searchAutoComplete.setAdapter(adapter);
                           // String select =   searchAutoComplete.getItemSelectedListener();

                            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                                @Override
                                public boolean onSuggestionSelect(int position) {
                                   // String select = searchAutoComplete.get()
                                   // searchView.setQuery();
                                    searchView.setQuery(adapter.getItem(position).toString(),true);
                                    return true;
                                }

                                @Override
                                public boolean onSuggestionClick(int position) {
                                    searchView.setQuery(adapter.getItem(position).toString(),true);
                                    return true;
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

    private void getAllProduk(){
        String url = "https://trading.my.id/android/allproduct.php";
        AndroidNetworking.get(url)
                .setTag(SearchResultsActivity.this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(objectsub.ObjectSub.class, new ParsedRequestListener<objectsub.ObjectSub>() {
                    @Override
                    public void onResponse(objectsub.ObjectSub response) {
                       // recyclerView.setVisibility(View.VISIBLE);
                       // adaptersub = new adaptersub(getApplicationContext(), response.sub1_kategori1);
                       // recyclerView.setAdapter(adaptersub);

                            final ArrayAdapter<objectsub.ObjectSub.Results> results = new ArrayAdapter<objectsub.ObjectSub.Results>(SearchResultsActivity.this, android.R.layout.simple_list_item_1, response.produk);
                            searchAutoComplete.setAdapter(results);

                        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                            @Override
                            public boolean onSuggestionSelect(int position) {
                                final int harga_disc;
                                final String harga = results.getItem(position).harga_asli ;
                                 String diskom;

                              /**  if (results.getItem(position).end_disc!=null){
                                    String valid_until = results.getItem(position).end_disc;
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                                    Date strDate = null;
                                    try {
                                        strDate = sdf.parse(valid_until);
                                        if (new Date().after(strDate)) {
                                            results.getItem(position).diskon = "0";
                                        }else if (new Date().equals(strDate)){
                                            results.getItem(position).diskon = "0";
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } **/

                                if ( results.getItem(position).diskon.equals("0")||results.getItem(position).diskon==null||results.getItem(position).diskon.equals("")){
                                    diskom = "1";
                                    harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom);
                                  //  holder.harga_asli.setVisibility(View.GONE);
                                  //  holder.diskon.setVisibility(View.GONE);
                                }else {
                                    diskom  = results.getItem(position).diskon;
                                    int total_disc_harga = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
                                    harga_disc = Integer.parseInt(harga)-total_disc_harga;
                                }

                                Intent intent = new Intent(context, ProdukDetailActivity.class);
                                intent.putExtra("id_produk",results.getItem(position).id_produk);
                                intent.putExtra("kode",results.getItem(position).kode);
                                intent.putExtra("id_member",results.getItem(position).id_member);
                                intent.putExtra("nama_produk",results.getItem(position).nama_produk);
                                intent.putExtra("harga_jual",Integer.toString(harga_disc));
                                intent.putExtra("keterangan_produk", results.getItem(position).keterangan);
                                intent.putExtra("image_o",results.getItem(position).gambar);
                                intent.putExtra("berat",results.getItem(position).berat);
                                intent.putExtra("harga_asli",results.getItem(position).harga_asli);
                                intent.putExtra("stok",results.getItem(position).stok);
                                intent.putExtra("diskon",results.getItem(position).diskon);
                                startActivity(intent);
                                return true;
                            }

                            @Override
                            public boolean onSuggestionClick(int position) {
                                final int harga_disc;
                                final String harga = results.getItem(position).harga_asli ;
                                String diskom;

                              /**  if (results.getItem(position).end_disc!=null){
                                    String valid_until = results.getItem(position).end_disc;
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                                    Date strDate = null;
                                    try {
                                        strDate = sdf.parse(valid_until);
                                        if (new Date().after(strDate)) {
                                            results.getItem(position).diskon = "0";
                                        }else if (new Date().equals(strDate)){
                                            results.getItem(position).diskon = "0";
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } **/

                                if ( results.getItem(position).diskon.equals("0")||results.getItem(position).diskon==null||results.getItem(position).diskon.equals("")){
                                    diskom = "1";
                                    harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom);
                                    //  holder.harga_asli.setVisibility(View.GONE);
                                    //  holder.diskon.setVisibility(View.GONE);
                                }else {
                                    diskom  = results.getItem(position).diskon;
                                    int total_disc_harga = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
                                    harga_disc = Integer.parseInt(harga)-total_disc_harga;
                                }

                                Intent intent = new Intent(context, ProdukDetailActivity.class);
                                intent.putExtra("id_produk",results.getItem(position).id_produk);
                                intent.putExtra("kode",results.getItem(position).kode);
                                intent.putExtra("id_member",results.getItem(position).id_member);
                                intent.putExtra("nama_produk",results.getItem(position).nama_produk);
                                intent.putExtra("harga_jual",Integer.toString(harga_disc));
                                intent.putExtra("keterangan_produk", results.getItem(position).keterangan);
                                intent.putExtra("image_o",results.getItem(position).gambar);
                                intent.putExtra("berat",results.getItem(position).berat);
                                intent.putExtra("harga_asli",results.getItem(position).harga_asli);
                                intent.putExtra("stok",results.getItem(position).stok);
                                intent.putExtra("diskon",results.getItem(position).diskon);
                                startActivity(intent);
                                return true;
                            }
                        });


                    }

                    @Override
                    public void onError(ANError anError) {


                    }
                });
    }

    // Menampilkan Data dari Database
    public void GetData(String produk) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, "https://trading.my.id/android/cari.php?nama_produk="+produk,
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
//       adaptersub.getFilter().filter(newText);
        recyclerView.setAdapter(null);
      //  GetData(newText);
        return true;
    }
}
