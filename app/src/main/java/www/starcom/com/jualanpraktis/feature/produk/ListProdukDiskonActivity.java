package www.starcom.com.jualanpraktis.feature.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import www.starcom.com.jualanpraktis.Kategori.SliderUtils;
import www.starcom.com.jualanpraktis.Kategori.ViewPagerAdapter;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SearchResultsActivity;
import www.starcom.com.jualanpraktis.adapter.ProdukPaginationAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityListProdukDiskonBinding;
import www.starcom.com.jualanpraktis.model.ListProduk;
import www.starcom.com.jualanpraktis.model.ResultsProduk;
import www.starcom.com.jualanpraktis.utils.EndlessRecyclerOnScrollListener;

import static com.android.volley.VolleyLog.TAG;

public class ListProdukDiskonActivity extends AppCompatActivity {

    Activity activity = ListProdukDiskonActivity.this;
    ActivityListProdukDiskonBinding binding;

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

    ProdukPaginationAdapter produkPaginationAdapter;
    List<ListProduk.ObjectSub.Results> produk;

    GridLayoutManager gridLayoutManager;
    private boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(activity,R.layout.activity_list_produk_diskon);
        AndroidNetworking.initialize(getApplicationContext());
        produkPaginationAdapter = new ProdukPaginationAdapter(activity);

        setSupportActionBar(binding.toolbar);
        binding.tvHint.setText("Cari dalam Produk Diskon");
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        binding.recyclerView.setLayoutManager(gridLayoutManager);

        requestQueue = Volley.newRequestQueue(activity);
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

        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(produkPaginationAdapter);


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
        timer.scheduleAtFixedRate(new ListProdukDiskonActivity.MyTimerTask(), 5000, 5000);
    }

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

                viewPagerAdapter = new ViewPagerAdapter(sliderimg, activity);
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
            if (ListProdukDiskonActivity.this != null) {
                ListProdukDiskonActivity.this.runOnUiThread(new Runnable() {
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


    //==== Produk ===/

    private void getAllProduk(final int currentPage) {
        if (currentPage==1){

            binding.recyclerView.setVisibility(View.GONE);
            binding.shimmer.setVisibility(View.VISIBLE);
            binding.shimmer.startShimmerAnimation();
        }
          String url = "https://jualanpraktis.net/android/diskon.php?page=" + currentPage;


        AndroidNetworking.get(url)
                .setTag(activity)
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

                            if (response.produkdata.size()!=0){
                                produkPaginationAdapter.addAll(response.produkdata);
                            }else{
                                produkPaginationAdapter.stop();
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



}