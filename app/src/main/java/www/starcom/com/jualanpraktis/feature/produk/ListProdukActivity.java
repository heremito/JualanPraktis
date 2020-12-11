package www.starcom.com.jualanpraktis.feature.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.arthurivanets.bottomsheets.BottomSheet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import www.starcom.com.jualanpraktis.Kategori.SliderUtils;
import www.starcom.com.jualanpraktis.Kategori.ViewPagerAdapter;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SearchResultsActivity;
import www.starcom.com.jualanpraktis.adapter.ProdukAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukEndlessScrollAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukPaginationAdapter;
import www.starcom.com.jualanpraktis.adapter.SubKategoriAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityListProdukBinding;
import www.starcom.com.jualanpraktis.model.ListProduk;
import www.starcom.com.jualanpraktis.model.ResultsProduk;
import www.starcom.com.jualanpraktis.utils.EndlessRecyclerOnScrollListener;
import www.starcom.com.jualanpraktis.utils.SimpleCustomBottomSheet;

import static com.android.volley.VolleyLog.TAG;

public class ListProdukActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Activity activity = ListProdukActivity.this;
    ActivityListProdukBinding binding;
    String title, id, status;

    //slider
    // LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    //  ViewPager viewPager;
    RequestQueue requestQueue;
    List<SliderUtils> sliderimg;
    String request_url = "https://jualanpraktis.net/android/banner.php";
    ViewPagerAdapter viewPagerAdapter;
    private Timer timer;

    //adapter
    ProdukAdapter adapter;
    ProdukPaginationAdapter produkPaginationAdapter;
    List<ListProduk.ObjectSub.Results> allproduct;

    //sub kategori
    ArrayList<HashMap<String, String>> subKategoriList = new ArrayList<>();
    ProdukEndlessScrollAdapter recyclerViewAdapter;
    List<ListProduk.ObjectSub.Results> rowsArrayList = new ArrayList<>();


    GridLayoutManager gridLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 60;
    private int currentPage = PAGE_START;

    private BottomSheet bottomSheet;
    RecyclerView recyclerViewSub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_list_produk);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_list_produk);

        AndroidNetworking.initialize(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("kategori");
            id = bundle.getString("id");
            status = bundle.getString("status");

        }

        produkPaginationAdapter = new ProdukPaginationAdapter(ListProdukActivity.this);

        setSupportActionBar(binding.toolbar);
        binding.tvHint.setText("Cari dalam " + title);
       gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
     //   gridLayoutManager.setAutoMeasureEnabled(true);

        binding.recyclerView.setLayoutManager(gridLayoutManager);
     //   binding.recyclerView.setHasFixedSize(true);

        //   binding.recyclerView.setNestedScrollingEnabled(false);
        binding. rvSubKategori.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        binding.rvSubKategori.setHasFixedSize(true);
      //  binding.rvSubKategori.setNestedScrollingEnabled(false);
        //ViewCompat.setNestedScrollingEnabled(binding.recyclerView, false);

        //  binding.recyclerView.setHasFixedSize(true);
        requestQueue = Volley.newRequestQueue(ListProdukActivity.this);
        sliderimg = new ArrayList<>();

        binding.cari.setFocusable(false);
        binding.cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SearchResultsActivity.class);
                activity.startActivity(intent);
            }
        });
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.showmore.setVisibility(View.GONE);


        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(produkPaginationAdapter);
     /**   binding.recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      //  dataVideo();

                       getAllProduk();
                    }
                }, 10);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // dataVideo();

                binding.recyclerView.setVisibility(View.GONE);
                binding.cvCari.setVisibility(View.GONE);
                binding.shimmer.setVisibility(View.VISIBLE);
                binding.shimmer.startShimmerAnimation();
                if (status.equals("allproduct")) {
                    getAllProduk();
                } else if (status.equals("produkIklan")) {
                    getAllProduk();
                } else {
                    // if (id.equals("33")){
                    getSubKategori();
                    // }
                    getAllProduk();
                }
            }
        }, 10); **/
     binding.showmore.setVisibility(View.GONE);
     binding.rvSubKategori.setVisibility(View.GONE);

        if (!status.equals("allproduct") & !status.equals("produkIklan")){
            getSubKategori();
        }

        getAllProduk(1);
        binding.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                getAllProduk(current_page);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

        sendRequest();

        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 5000, 5000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_produk_menu, menu);

        final MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getSubKategori() {

        final String url = "https://jualanpraktis.net/android/subkategori.php";
        AndroidNetworking.post(url)
                .addBodyParameter("id_kategori_produk", id)
                .setTag(activity)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.showmore.setVisibility(View.VISIBLE);
                        try {
                            subKategoriList.clear();
                            JSONArray array = response.getJSONArray("subkategori");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String, String> data = new HashMap<>();
                                data.put("id", jsonObject.getString("id_sub_kategori_produk"));
                                data.put("subkategori", jsonObject.getString("sub_kategori_produk"));
                                data.put("gambar", jsonObject.getString("img"));
                                //     data.put("created_at",jsonObject.getString("created_at"));
                                subKategoriList.add(data);
                            }

                             SubKategoriAdapter adapter = new SubKategoriAdapter(activity, subKategoriList, binding.showmore,"main");
                            binding.rvSubKategori.setAdapter(adapter);
                            binding.rvSubKategori.setVisibility(View.VISIBLE);
                            binding.showmore.setVisibility(View.VISIBLE);

                            binding.showmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    bottomSheet = new SimpleCustomBottomSheet(activity);
                                    bottomSheet.show(true);

                                    SubKategoriAdapter adapter = new SubKategoriAdapter(activity, subKategoriList, binding.showmore,"bottom");
                                    recyclerViewSub = findViewById(R.id.recyclerViewSub);
                                    ImageView close = findViewById(R.id.close);
                                    recyclerViewSub.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
                                    recyclerViewSub.setAdapter(adapter);

                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            bottomSheet.dismiss();
                                        }
                                    });



                                }
                            });


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
                            Toast.makeText(activity, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(activity, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void getData() {

        binding.recyclerView.setVisibility(View.GONE);
        binding.cvCari.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmerAnimation();
        HashMap<String, String> param = new HashMap<>();
        String url = "";

        if (status.equals("produkIklan")) {
            url = "https://jualanpraktis.net/android/produk_tenan.php";
            param.put("id_member", id);
        } else {
            url = "https://jualanpraktis.net/android/produk_kategori.php?page=" + currentPage;
            param.put("id_kategori_produk", id);
        }

        AndroidNetworking.post(url)
                .addBodyParameter(param)
                .setTag(ListProdukActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ResultsProduk.class, new ParsedRequestListener<ResultsProduk>() {
                    @Override
                    public void onResponse(ResultsProduk response) {

                        binding.recyclerView.setVisibility(View.VISIBLE);
                        if (currentPage==1){
                            binding.shimmer.stopShimmerAnimation();
                            binding.shimmer.setVisibility(View.GONE);
                            //  adapter.addAll(response.getListVideos());
                            if (currentPage <= TOTAL_PAGES)
                                produkPaginationAdapter.addLoadingFooter();
                            else
                                isLastPage = true;
                        }else {
                            produkPaginationAdapter.removeLoadingFooter();
                            isLoading = false;

                            if (currentPage != TOTAL_PAGES)
                                produkPaginationAdapter.addLoadingFooter();
                            else
                                isLastPage = true;

                        }

                     //   if (response.semuaproduk.size()!=0){
                     //       produkPaginationAdapter.addAll(response.produkKategori);
                     //   }



                    }

                    @Override
                    public void onError(ANError anError) {
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllProduk(final int currentPage) {
        if (currentPage==1){

            binding.recyclerView.setVisibility(View.GONE);
            binding.shimmer.setVisibility(View.VISIBLE);
            binding.shimmer.startShimmerAnimation();
        }
        String url = "";
        if (status.equals("allproduct")){
             url = "https://jualanpraktis.net/android/produk.php?page=" + currentPage;
             binding.showmore.setVisibility(View.GONE);
        }else if (status.equals("produkIklan")){
            url = "https://jualanpraktis.net/android/produk_tenan.php?page=" + currentPage +"&id_member="+id;
            binding.showmore.setVisibility(View.GONE);
        }
        else{
            url = "https://jualanpraktis.net/android/produk_kategori.php?page=" + currentPage + "&id_kategori_produk="+id;
        }

        AndroidNetworking.get(url)
                .setTag(ListProdukActivity.this)
                .setPriority(Priority.LOW)
               // .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(ResultsProduk.class, new ParsedRequestListener<ResultsProduk>() {
                    @Override
                    public void onResponse(ResultsProduk response) {
                        // swipeRefreshLayout.setRefreshing(false);

                        binding.recyclerView.setVisibility(View.VISIBLE);
                      //  rowsArrayList = response.semuaproduk;
                      //  adapter = new ProdukAdapter(getApplicationContext(),response.semuaproduk, "list");
                     //   binding.recyclerView.setAdapter(adapter);

                        if (currentPage==1){
                            binding.shimmer.stopShimmerAnimation();
                            binding.shimmer.setVisibility(View.GONE);
                            binding.cvCari.setVisibility(View.GONE);
                            //  adapter.addAll(response.getListVideos());
                           // if (currentPage <= TOTAL_PAGES)
                                produkPaginationAdapter.addLoadingFooter();
                           // else
                             //   isLastPage = true;
                        }else {
                            produkPaginationAdapter.removeLoadingFooter();
                            isLoading = false;

                          //  if (currentPage != TOTAL_PAGES)
                                produkPaginationAdapter.addLoadingFooter();
                          //  else
                          //      isLastPage = true;

                        }
                        if (status.equals("allproduct")){
                            if (response.semuaproduk.size()!=0){
                                produkPaginationAdapter.addAll(response.semuaproduk);
                            }else {
                                produkPaginationAdapter.stop();
                            }
                        }else{
                            if (response.produkdata.size()!=0){
                                produkPaginationAdapter.addAll(response.produkdata);
                            }else{
                                produkPaginationAdapter.stop();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //get data slider

    public void sendRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    SliderUtils sliderUtils = new SliderUtils();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        final String UrlImage = "https://jualanpraktis.net/img/";
                        final String Image = jsonObject.getString("image");
                        final Uri uri = Uri.parse(UrlImage + Image);
                        sliderUtils.setSliderImageUrl(uri.toString());
                        Log.d(TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderimg.add(sliderUtils);
                }

                viewPagerAdapter = new ViewPagerAdapter(sliderimg, ListProdukActivity.this);
                binding.pagerslide.setAdapter(viewPagerAdapter);
                /*
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.notactive_dots));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dots));
                */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (ListProdukActivity.this != null) {
                ListProdukActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.pagerslide.getCurrentItem() == 0) {
                            binding.pagerslide.setCurrentItem(1);
                        } else if (binding.pagerslide.getCurrentItem() == 1) {
                            binding.pagerslide.setCurrentItem(2);
                        } else if (binding.pagerslide.getCurrentItem() == 2) {
                            binding.pagerslide.setCurrentItem(3);
                        } else {
                            binding.pagerslide.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }


}
